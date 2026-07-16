package com.vuemov.controller;

import com.vuemov.model.Grammar;
import com.vuemov.model.Lesson;
import com.vuemov.model.Vocabulary;
import com.vuemov.repository.GrammarRepository;
import com.vuemov.repository.LessonRepository;
import com.vuemov.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/zhongwen")
@CrossOrigin(origins = "*")
public class ZhongwenApiController {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private GrammarRepository grammarRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private String toSlug(String text) {
        return text.toLowerCase()
            .replaceAll("đ", "d")
            .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
            .replaceAll("[èéẹẻẽêềếệểễ]", "e")
            .replaceAll("[ìíịỉĩ]", "i")
            .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
            .replaceAll("[ùúụủũưừứựửữ]", "u")
            .replaceAll("[ỳýỵỷỹ]", "y")
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        List<Lesson> lessons = lessonRepository.findAll();

        List<Map<String, Object>> practiceLessons = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getVideoUrl() != null && !l.getVideoUrl().isEmpty()) {
                Map<String, Object> m = new HashMap<>();
                m.put("title", l.getTitle());
                m.put("hskLevel", "HSK" + l.getHskLevel());
                m.put("sourceType", "youtube");
                m.put("duration", "05:00");
                m.put("views", 1000);
                practiceLessons.add(m);
            }
        }

        List<Map<String, Object>> categories = new ArrayList<>();
        Map<String, Object> cat1 = new HashMap<>();
        cat1.put("name", "Luyện tập Shadowing");
        cat1.put("lessonCount", practiceLessons.size());
        cat1.put("lessons", practiceLessons);
        categories.add(cat1);

        List<Map<String, Object>> studyLessons = new ArrayList<>();
        for (Lesson l : lessons) {
            Map<String, Object> m = new HashMap<>();
            m.put("title", l.getTitle());
            m.put("hskLevel", "HSK" + l.getHskLevel());
            m.put("sourceType", "study");
            m.put("duration", l.getAudioUrl() != null ? "03:00" : "05:00");
            m.put("views", 500);
            studyLessons.add(m);
        }
        Map<String, Object> cat2 = new HashMap<>();
        cat2.put("name", "Bài học theo giáo trình");
        cat2.put("lessonCount", studyLessons.size());
        cat2.put("lessons", studyLessons);
        categories.add(cat2);

        Map<String, Object> result = new HashMap<>();
        result.put("data", categories);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vocabulary")
    public ResponseEntity<Map<String, Object>> getVocabulary() {
        List<Map<String, Object>> sections = new ArrayList<>();

        for (int level = 1; level <= 9; level++) {
            List<Vocabulary> words = vocabularyRepository.findByHskLevel(level);
            if (!words.isEmpty()) {
                Map<String, Object> card = new HashMap<>();
                card.put("name", "HSK " + level + " (3.0)");
                card.put("cardCount", words.size());
                card.put("learners", 1000 + (9 - level) * 500);

                Map<String, Object> section = new HashMap<>();
                section.put("name", "Từ Vựng HSK 3.0");
                section.put("expectedCount", 1);
                section.put("cards", Collections.singletonList(card));
                sections.add(section);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", sections);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/vocabulary/{id}/words")
    public ResponseEntity<Map<String, Object>> getVocabDeck(@PathVariable String id) {
        int level = 0;
        for (int i = 1; i <= 9; i++) {
            if (id.contains("hsk" + i) || id.contains("HSK-" + i) || id.contains(toSlug("HSK " + i))) {
                level = i;
                break;
            }
        }
        if (level == 0) {
            for (int i = 1; i <= 9; i++) {
                if (id.contains(String.valueOf(i))) {
                    level = i;
                    break;
                }
            }
        }
        if (level == 0) level = 1;

        List<Vocabulary> words = vocabularyRepository.findByHskLevel(level);
        List<Map<String, Object>> wordList = new ArrayList<>();
        for (Vocabulary v : words) {
            Map<String, Object> w = new HashMap<>();
            w.put("hanzi", v.getSimplifiedChinese());
            w.put("wordType", v.getPartOfSpeech());
            w.put("pinyin", v.getPinyin());
            w.put("meaning", v.getVietnameseMeaning());
            w.put("definition", v.getEnglishMeaning());
            w.put("exampleCn", v.getExampleSentence());
            w.put("exampleVi", v.getExampleTranslation());
            wordList.add(w);
        }

        Map<String, Object> deck = new HashMap<>();
        deck.put("_id", "hsk" + level);
        deck.put("title", "HSK " + level + " (3.0)");
        deck.put("hskLevel", Collections.singletonList(String.valueOf(level)));
        deck.put("totalCards", words.size());

        List<Map<String, Object>> units = new ArrayList<>();
        Map<String, Object> unit = new HashMap<>();
        unit.put("name", "Tất cả từ vựng");
        unit.put("order", 1);
        unit.put("studied", 0);
        unit.put("total", words.size());
        units.add(unit);
        deck.put("units", units);
        deck.put("words", wordList);

        Map<String, Object> result = new HashMap<>();
        result.put("data", deck);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/grammar")
    public ResponseEntity<Map<String, Object>> getGrammar() {
        List<Grammar> all = grammarRepository.findAll();
        List<Map<String, Object>> items = new ArrayList<>();
        for (Grammar g : all) {
            Map<String, Object> item = new HashMap<>();
            item.put("hanzi", g.getTitle());
            item.put("pinyin", g.getGrammarPattern() != null ? g.getGrammarPattern() : "");
            item.put("vietnamese", g.getExplanation() != null ? g.getExplanation().substring(0, Math.min(100, g.getExplanation().length())) : "");
            item.put("description", g.getExplanation() != null ? g.getExplanation() : "");
            item.put("hskLevel", "HSK " + g.getHskLevel());
            item.put("structureCount", g.getExampleSentences() != null ? g.getExampleSentences().size() : 1);
            item.put("tag", "hsk" + g.getHskLevel());
            item.put("_id", g.getId());
            item.put("hasDetail", false);
            items.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", items);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/lessons")
    public ResponseEntity<Map<String, Object>> getLessons() {
        List<Lesson> all = lessonRepository.findAll();
        List<Map<String, Object>> items = new ArrayList<>();
        for (Lesson l : all) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", l.getTitle());
            item.put("hskLevel", "HSK" + l.getHskLevel());
            item.put("sourceType", "youtube");
            item.put("duration", "05:00");
            item.put("views", 1000);
            items.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", items);
        return ResponseEntity.ok(result);
    }
}
