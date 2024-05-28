import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareAndHighlight {
    public static void main(String[] args) throws IOException {
        // 원본 연습 문제와 풀이용 문제에서 정답을 추출
        Map<String, String> originalAnswers = getAnswersFromDoc("original.docx");
        Map<String, String> modifiedAnswers = getAnswersFromDoc("test.docx");

        // 정답을 비교하고 채점 결과를 "scoring_result.docx"로 저장
        int correctCount = compareAndHighlightAnswers(originalAnswers, modifiedAnswers, "test.docx", "scoring_result.docx");

        // 맞춘 문제 개수와 총 문제 개수를 콘솔에 출력
        int totalCount = originalAnswers.size();
        System.out.println(correctCount + "/" + totalCount + " correct");
    }

    /**
     * 주어진 파일 경로에서 질문과 정답을 추출해 맵으로 반환
     *
     * @param filePath 파일 경로
     * @return 질문과 정답을 포함하는 맵
     * @throws IOException 파일 읽기 오류 발생 시
     */
    private static Map<String, String> getAnswersFromDoc(String filePath) throws IOException {
        Map<String, String> answers = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            String questionNumber = null;

            for (XWPFParagraph para : paragraphs) {
                String text = para.getText().trim();
                if (text.startsWith("Q")) {
                    questionNumber = text;
                }
                if (text.startsWith("Answer:")) {
                    if (questionNumber != null) {
                        String[] parts = text.split("Answer:");
                        if (parts.length > 1) {
                            answers.put(questionNumber, parts[1].trim());
                        } else {
                            answers.put(questionNumber, "");
                        }
                        questionNumber = null;
                    }
                }
            }
        }
        return answers;
    }

    /**
     * 원본 정답과 입력한 정답을 비교하여 결과를 문서로 저장
     * 틀린 문제를 빨간색으로 표시하고, 문서 상단에 맞힌 문제 개수와 총 문제 개수를 표시
     *
     * @param originalAnswers 원본 정답 맵
     * @param modifiedAnswers 수정된 정답 맵
     * @param inputFilePath 수정된 문서 파일 경로
     * @param outputFilePath 결과를 저장할 파일 경로
     * @return 맞힌 문제의 개수
     * @throws IOException 파일 읽기/쓰기 오류 발생 시
     */
    private static int compareAndHighlightAnswers(Map<String, String> originalAnswers, Map<String, String> modifiedAnswers, String inputFilePath, String outputFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            int correctCount = 0;
            int totalCount = originalAnswers.size();

            // 정답을 비교하고 틀린 문제를 빨간색으로 표시
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (text.startsWith("Q")) {
                    String question = text;
                    if (originalAnswers.containsKey(question)) {
                        String originalAnswer = originalAnswers.get(question);
                        String modifiedAnswer = modifiedAnswers.getOrDefault(question, "N/A");
                        // 대소문자 구분 없이 정답을 비교하고, 여러 정답을 지원합니다.
                        boolean isCorrect = compareAnswers(originalAnswer, modifiedAnswer);

                        if (!isCorrect) {
                            // 질문 문단과 그 다음 문단을 모두 빨간색으로 설정
                            int paraIndex = document.getParagraphs().indexOf(para);
                            while (paraIndex < document.getParagraphs().size()) {
                                XWPFParagraph answerPara = document.getParagraphArray(paraIndex);
                                for (XWPFRun run : answerPara.getRuns()) {
                                    run.setColor("FF0000"); // 빨간색
                                }
                                if (answerPara.getText().startsWith("Answer:")) {
                                    break;
                                }
                                paraIndex++;
                            }
                        } else {
                            correctCount++;
                        }
                    }
                }
            }

            // 맞힌 문제 개수와 총 문제 개수를 문서 상단에 추가
            XWPFParagraph firstParagraph = document.createParagraph();
            XWPFRun run = firstParagraph.createRun();
            run.setText("맞힌 개수: "+ correctCount + " / " + totalCount);
            run.setColor("FF0000"); // 빨간색
            run.setFontSize(16);

            // 새 문단을 문서 맨 앞에 삽입
            document.setParagraph(firstParagraph, 0);

            // 결과 문서를 저장
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                document.write(fos);
            }

            return correctCount;
        }
    }

    /**
     * 두 개의 정답 문자열을 대소문자 구분 없이 비교합니다.
     * 여러 정답이 있을 경우 콤마(,)로 구분하여 비교합니다.
     *
     * @param originalAnswer 원본 정답 문자열
     * @param modifiedAnswer 수정된 정답 문자열
     * @return 정답이 일치하면 true, 아니면 false
     */
    private static boolean compareAnswers(String originalAnswer, String modifiedAnswer) {
        String[] originalAnswers = originalAnswer.split(",");
        String[] modifiedAnswers = modifiedAnswer.split(",");

        for (String modAns : modifiedAnswers) {
            boolean matchFound = false;
            for (String origAns : originalAnswers) {
                if (origAns.trim().equalsIgnoreCase(modAns.trim())) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                return false;
            }
        }
        return true;
    }
}