package com.vuemov.service;

import com.vuemov.model.*;
import com.vuemov.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataSeederService {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private GrammarRepository grammarRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    public int importVocabularyFromCsv(String... filePaths) {
        int total = 0;
        for (String fp : filePaths) {
            total += importVocabularyFromSingleCsv(fp);
        }
        return total;
    }

    private int importVocabularyFromSingleCsv(String filePath) {
        int count = 0;
        try {
            List<Vocabulary> batch = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new ClassPathResource(filePath).getInputStream(),
                    StandardCharsets.UTF_8
                )
            );
            String line = reader.readLine();
            if (line == null) return 0;
            Map<String, Integer> headerMap = parseHeader(line);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] fields = parseCsvLine(line);
                if (fields.length < 4) continue;

                Vocabulary v = new Vocabulary();
                v.setSimplifiedChinese(getField(fields, headerMap, "simplifiedChinese", "Simplified", "word"));
                v.setPinyin(getField(fields, headerMap, "pinyin", "Pinyin"));
                v.setEnglishMeaning(getField(fields, headerMap, "englishMeaning", "English"));
                v.setVietnameseMeaning(getField(fields, headerMap, "vietnameseMeaning", "Vietnamese"));
                v.setPartOfSpeech(getField(fields, headerMap, "partOfSpeech", "PartOfSpeech"));
                
                String levelStr = getField(fields, headerMap, "hskLevel", "HSK Level", "HSK_Level", "level");
                v.setHskLevel(levelStr != null ? Integer.parseInt(levelStr.trim()) : 1);
                
                v.setExampleSentence(getField(fields, headerMap, "exampleSentence", "Example Sentence", "example"));
                v.setExampleTranslation(getField(fields, headerMap, "exampleTranslation", "Example Translation", "translation"));
                v.setTraditionalChinese(getField(fields, headerMap, "traditionalChinese", "Traditional"));
                v.setAudioUrl(getField(fields, headerMap, "audioUrl", "Audio"));
                v.setImageUrl(getField(fields, headerMap, "imageUrl", "Image"));
                
                v.setMnemonicTips(new ArrayList<>());
                v.setCreatedAt(LocalDateTime.now());
                v.setUpdatedAt(LocalDateTime.now());
                
                batch.add(v);
                
                if (batch.size() >= 100) {
                    vocabularyRepository.saveAll(batch);
                    count += batch.size();
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                vocabularyRepository.saveAll(batch);
                count += batch.size();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int importGrammarFromCsv(String filePath) {
        int count = 0;
        try {
            List<Grammar> batch = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new ClassPathResource(filePath).getInputStream(),
                    StandardCharsets.UTF_8
                )
            );
            String line = reader.readLine();
            if (line == null) return 0;
            Map<String, Integer> headerMap = parseHeader(line);

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] fields = parseCsvLine(line);
                if (fields.length < 3) continue;

                Grammar g = new Grammar();
                g.setTitle(getField(fields, headerMap, "title", "Title", "grammar"));
                g.setExplanation(getField(fields, headerMap, "explanation", "Explanation", "explain"));
                
                String levelStr = getField(fields, headerMap, "hskLevel", "HSK Level", "level");
                g.setHskLevel(levelStr != null ? Integer.parseInt(levelStr.trim()) : 1);
                
                g.setGrammarPattern(getField(fields, headerMap, "grammarPattern", "pattern", "GrammarPattern"));
                g.setUsageNote(getField(fields, headerMap, "usageNote", "usage", "UsageNote"));
                g.setVideoUrl(getField(fields, headerMap, "videoUrl", "video", "VideoUrl"));
                
                String examples = getField(fields, headerMap, "exampleSentences", "examples", "Examples");
                if (examples != null) {
                    g.setExampleSentences(Arrays.asList(examples.split("\\|")));
                } else {
                    g.setExampleSentences(new ArrayList<>());
                }
                
                String translations = getField(fields, headerMap, "exampleTranslations", "translations", "Translations");
                if (translations != null) {
                    g.setExampleTranslations(Arrays.asList(translations.split("\\|")));
                } else {
                    g.setExampleTranslations(new ArrayList<>());
                }
                
                g.setSimilarGrammar(new ArrayList<>());
                g.setCreatedAt(LocalDateTime.now());
                g.setUpdatedAt(LocalDateTime.now());
                
                batch.add(g);
                
                if (batch.size() >= 100) {
                    grammarRepository.saveAll(batch);
                    count += batch.size();
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                grammarRepository.saveAll(batch);
                count += batch.size();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int createSeedLessons() {
        int count = 0;
        String[][] lessonData = {
            {"1", "Chào hỏi cơ bản", "Học cách chào hỏi và giới thiệu bản thân bằng tiếng Trung", "https://www.youtube.com/embed/embed_url_hsk1_1", "https://audio.example.com/hsk1_1.mp3"},
            {"1", "Số đếm và màu sắc", "Học số đếm từ 1-10 và các màu sắc cơ bản", "https://www.youtube.com/embed/embed_url_hsk1_2", "https://audio.example.com/hsk1_2.mp3"},
            {"1", "Gia đình và bạn bè", "Từ vựng về gia đình, bạn bè và quan hệ xã hội", "https://www.youtube.com/embed/embed_url_hsk1_3", "https://audio.example.com/hsk1_3.mp3"},
            {"2", "Thời gian và ngày tháng", "Học cách nói về thời gian, ngày tháng và mùa", "https://www.youtube.com/embed/embed_url_hsk2_1", "https://audio.example.com/hsk2_1.mp3"},
            {"2", "Đồ ăn và ẩm thực", "Từ vựng về đồ ăn, thức uống và nhà hàng", "https://www.youtube.com/embed/embed_url_hsk2_2", "https://audio.example.com/hsk2_2.mp3"},
            {"2", "Mua sắm và giá cả", "Học từ vựng mua sắm, hỏi giá và trả giá", "https://www.youtube.com/embed/embed_url_hsk2_3", "https://audio.example.com/hsk2_3.mp3"},
            {"3", "Du lịch và phương hướng", "Từ vựng du lịch, hỏi đường và chỉ đường", "https://www.youtube.com/embed/embed_url_hsk3_1", "https://audio.example.com/hsk3_1.mp3"},
            {"3", "Sức khỏe và bệnh viện", "Học từ vựng về sức khỏe, bệnh tật và thuốc men", "https://www.youtube.com/embed/embed_url_hsk3_2", "https://audio.example.com/hsk3_2.mp3"},
            {"3", "Công việc và nghề nghiệp", "Từ vựng về công việc, nghề nghiệp và văn phòng", "https://www.youtube.com/embed/embed_url_hsk3_3", "https://audio.example.com/hsk3_3.mp3"},
            {"4", "Giáo dục và học tập", "Từ vựng về trường học, môn học và thi cử", "https://www.youtube.com/embed/embed_url_hsk4_1", "https://audio.example.com/hsk4_1.mp3"},
            {"4", "Kinh tế và tài chính", "Học từ vựng về kinh tế, ngân hàng và đầu tư", "https://www.youtube.com/embed/embed_url_hsk4_2", "https://audio.example.com/hsk4_2.mp3"},
            {"4", "Văn hóa và truyền thống", "Từ vựng về văn hóa, lễ hội và phong tục Trung Quốc", "https://www.youtube.com/embed/embed_url_hsk4_3", "https://audio.example.com/hsk4_3.mp3"},
            {"5", "Khoa học và công nghệ", "Từ vựng về khoa học, công nghệ và internet", "https://www.youtube.com/embed/embed_url_hsk5_1", "https://audio.example.com/hsk5_1.mp3"},
            {"5", "Môi trường và thiên nhiên", "Học từ vựng về môi trường, thời tiết và thiên tai", "https://www.youtube.com/embed/embed_url_hsk5_2", "https://audio.example.com/hsk5_2.mp3"},
            {"5", "Chính trị và xã hội", "Từ vựng về chính trị, xã hội và luật pháp", "https://www.youtube.com/embed/embed_url_hsk5_3", "https://audio.example.com/hsk5_3.mp3"},
            {"6", "Văn học và nghệ thuật", "Từ vựng về văn học, nghệ thuật và âm nhạc", "https://www.youtube.com/embed/embed_url_hsk6_1", "https://audio.example.com/hsk6_1.mp3"},
            {"6", "Triết học và tư tưởng", "Học từ vựng về triết học, tư tưởng và tôn giáo", "https://www.youtube.com/embed/embed_url_hsk6_2", "https://audio.example.com/hsk6_2.mp3"},
            {"6", "Y học và sức khỏe cộng đồng", "Từ vựng chuyên ngành y học và sức khỏe", "https://www.youtube.com/embed/embed_url_hsk6_3", "https://audio.example.com/hsk6_3.mp3"},
            {"7", "Kinh doanh quốc tế", "Từ vựng chuyên ngành kinh doanh và thương mại", "https://www.youtube.com/embed/embed_url_hsk7_1", "https://audio.example.com/hsk7_1.mp3"},
            {"7", "Luật và chính sách", "Học từ vựng pháp luật và chính sách công", "https://www.youtube.com/embed/embed_url_hsk7_2", "https://audio.example.com/hsk7_2.mp3"},
            {"8", "Báo chí và truyền thông", "Từ vựng báo chí, truyền thông và xuất bản", "https://www.youtube.com/embed/embed_url_hsk8_1", "https://audio.example.com/hsk8_1.mp3"},
            {"8", "Nghiên cứu học thuật", "Từ vựng nghiên cứu khoa học và học thuật", "https://www.youtube.com/embed/embed_url_hsk8_2", "https://audio.example.com/hsk8_2.mp3"},
            {"9", "Văn hóa Trung Hoa cổ điển", "Từ vựng về văn hóa cổ điển và thành ngữ", "https://www.youtube.com/embed/embed_url_hsk9_1", "https://audio.example.com/hsk9_1.mp3"},
            {"9", "Chuyên ngành nâng cao", "Từ vựng chuyên ngành nâng cao theo lĩnh vực", "https://www.youtube.com/embed/embed_url_hsk9_2", "https://audio.example.com/hsk9_2.mp3"}
        };

        if (lessonRepository.count() == 0) {
            for (String[] data : lessonData) {
                Lesson lesson = new Lesson(
                    data[1],
                    data[2],
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[3].substring(data[3].lastIndexOf("_") + 1))
                );
                lesson.setContent("Nội dung bài học " + data[1] + " - Bài học chi tiết về " + data[2].toLowerCase() + ". Học viên sẽ được học từ vựng, ngữ pháp và làm bài tập thực hành.");
                lesson.setVideoUrl(data[3]);
                lesson.setAudioUrl(data[4]);
                lesson.setCreatedAt(LocalDateTime.now());
                lesson.setUpdatedAt(LocalDateTime.now());
                
                List<Vocabulary> levelVocab = vocabularyRepository.findByHskLevel(Integer.parseInt(data[0]));
                List<String> vocabIds = levelVocab.stream()
                    .map(Vocabulary::getId)
                    .limit(10)
                    .toList();
                lesson.setVocabularyIds(new ArrayList<>(vocabIds));
                
                List<Grammar> levelGrammar = grammarRepository.findByHskLevel(Integer.parseInt(data[0]));
                List<String> grammarIds = levelGrammar.stream()
                    .map(Grammar::getId)
                    .limit(5)
                    .toList();
                lesson.setGrammarIds(new ArrayList<>(grammarIds));
                
                lessonRepository.save(lesson);
                count++;
            }
        }
        return count;
    }

    public int importPracticeVideosFromCsv(String filePath) {
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new ClassPathResource(filePath).getInputStream(),
                    StandardCharsets.UTF_8
                )
            );
            String line = reader.readLine();
            if (line == null) return 0;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] fields = parseCsvLine(line);
                if (fields.length < 3) continue;
                
                String title = fields[0];
                int hskLevel = Integer.parseInt(fields[1].trim());
                String videoUrl = fields[2].trim();
                
                Lesson lesson = new Lesson(title, "Practice: " + title, hskLevel, hskLevel);
                lesson.setContent("Shadowing practice video for HSK " + hskLevel);
                lesson.setVideoUrl(videoUrl);
                lesson.setCreatedAt(LocalDateTime.now());
                lesson.setUpdatedAt(LocalDateTime.now());
                
                List<Vocabulary> levelVocab = vocabularyRepository.findByHskLevel(hskLevel);
                List<String> vocabIds = levelVocab.stream()
                    .map(Vocabulary::getId)
                    .limit(10)
                    .toList();
                lesson.setVocabularyIds(new ArrayList<>(vocabIds));
                
                List<Grammar> levelGrammar = grammarRepository.findByHskLevel(hskLevel);
                List<String> grammarIds = levelGrammar.stream()
                    .map(Grammar::getId)
                    .limit(5)
                    .toList();
                lesson.setGrammarIds(new ArrayList<>(grammarIds));
                
                lessonRepository.save(lesson);
                count++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int createSeedExercises() {
        int count = 0;
        if (exerciseRepository.count() == 0) {
            Random random = new Random();
            List<Vocabulary> allVocab = vocabularyRepository.findAll();
            
            List<Exercise> exercises = new ArrayList<>();
            
            for (int level = 1; level <= 9; level++) {
                int currentLevel = level;
                List<Vocabulary> levelVocab = allVocab.stream()
                    .filter(v -> v.getHskLevel() == currentLevel)
                    .toList();
                if (levelVocab.size() < 4) continue;
                
                for (int i = 0; i < 5; i++) {
                    String word = levelVocab.get(i % levelVocab.size()).getSimplifiedChinese();
                    Exercise e = new Exercise("Bài tập cấp " + level, "multiple_choice", level, "Chọn nghĩa đúng của từ: " + word);
                    e.setCorrectAnswer(levelVocab.get(i % levelVocab.size()).getVietnameseMeaning());
                    e.setCreatedAt(LocalDateTime.now());
                    exercises.add(e);
                    count++;
                }
            }
            
            exerciseRepository.saveAll(exercises);
        }
        return count;
    }

    public int createSeedAchievements() {
        int count = 0;
        if (achievementRepository.count() == 0) {
            List<Achievement> achievements = new ArrayList<>();
            Achievement[] achievementArr = {
                new Achievement("newbie", "Học viên mới", "Hoàn thành bài học đầu tiên", "milestone"),
                new Achievement("diligent", "Chăm chỉ", "Học 7 ngày liên tiếp", "streak"),
                new Achievement("explorer", "Nhà thám hiểm", "Hoàn thành 10 bài học", "milestone"),
                new Achievement("vocab_master", "Bậc thầy từ vựng", "Học 100 từ vựng", "milestone"),
                new Achievement("grammarian", "Ngữ pháp gia", "Hoàn thành 20 bài ngữ pháp", "milestone"),
                new Achievement("hsk1", "HSK 1", "Vượt qua HSK cấp 1", "hsk"),
                new Achievement("hsk2", "HSK 2", "Vượt qua HSK cấp 2", "hsk"),
                new Achievement("hsk3", "HSK 3", "Vượt qua HSK cấp 3", "hsk"),
                new Achievement("hsk4", "HSK 4", "Vượt qua HSK cấp 4", "hsk"),
                new Achievement("hsk5", "HSK 5", "Vượt qua HSK cấp 5", "hsk"),
                new Achievement("hsk6", "HSK 6", "Vượt qua HSK cấp 6", "hsk"),
                new Achievement("champion", "Nhà vô địch", "Đứng đầu bảng xếp hạng", "social"),
                new Achievement("practice_50", "Cần mẫn", "Hoàn thành 50 bài tập", "practice"),
                new Achievement("practice_100", "Siêng năng", "Hoàn thành 100 bài tập", "practice"),
                new Achievement("proficient", "Thành thạo", "Đạt 90%+ điểm trong 10 bài kiểm tra", "milestone")
            };
            for (Achievement a : achievementArr) {
                a.setCreatedAt(LocalDateTime.now());
            }
            achievements.addAll(Arrays.asList(achievementArr));
            achievementRepository.saveAll(achievements);
            count = achievements.size();
        }
        return count;
    }

    private Map<String, Integer> parseHeader(String headerLine) {
        Map<String, Integer> map = new HashMap<>();
        String[] cols = parseCsvLine(headerLine);
        for (int i = 0; i < cols.length; i++) {
            map.put(cols[i].trim().toLowerCase().replace(" ", ""), i);
        }
        return map;
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }
        fields.add(sb.toString().trim());
        return fields.toArray(new String[0]);
    }

    private String getField(String[] fields, Map<String, Integer> headerMap, String... possibleKeys) {
        for (String key : possibleKeys) {
            String normalizedKey = key.toLowerCase().replace(" ", "").replace("_", "");
            for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                String headerKey = entry.getKey().toLowerCase().replace(" ", "").replace("_", "");
                if (headerKey.equals(normalizedKey)) {
                    int idx = entry.getValue();
                    if (idx < fields.length) return fields[idx];
                }
            }
        }
        return null;
    }
}
