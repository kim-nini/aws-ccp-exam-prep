# AWS CCP Exam Prep - Grading Tool

이 프로젝트는 AWS Certified Cloud Practitioner (CCP) 시험 준비를 위한 자동 채점 도구입니다.<br>
주어진 **원본 연습 문제(original.docx)** 와 **풀이용 문제(test.docx)** 에서 정답을 추출하여 비교하고, <br>
틀린 문제는 빨간색으로 표시하며 맞힌 문제의 개수와 총 문제 개수를 문서 상단에 표시합니다.

## 프로젝트 구조
![image](https://github.com/kim-nini/aws-ccp-exam-prep/assets/144877020/663445b7-c9b8-4ad8-924c-c0d132bef47c)

## 요구 사항
- Java 11 이상
- Maven

## 사용 방법
1. 프로젝트를 클론합니다.
   ```bash
   git clone https://github.com/kim-nini/aws-ccp-exam-prep.git
   cd aws-ccp-exam-prep
   ```
   <br>
2. 루트 폴더에 포함된 풀이용 문제 파일(`test.docx`)을 실행한 후<br>
🔸Answer: 뒤에 **띄어쓰기 없이** 정답을 입력합니다.<br>
🔸정답은 **대문자**만 허용합니다.<br>
🔸예시를 위해 Q1과 Q2는 정답이 입력되어 있습니다. 삭제후 풀어 나가시면 됩니다. <br>
<br> ![스크린샷 2024-05-22 155651](https://github.com/kim-nini/aws-ccp-exam-prep/assets/144877020/3d6daed0-a96d-4f59-bdb5-41d97419e84b)

3. `CompareAndHighlight` 클래스를 실행하여 채점 결과를 생성합니다. 

   - (권장✅) IDE를 사용하여 실행합니다.
   - 또는 Maven을 사용하여 프로젝트를 빌드하고 실행합니다.
     ```bash
     mvn clean install
     mvn exec:java -Dexec.mainClass="CompareAndHighlight"
     ```
     <br>
4. 루트폴더에 생성된 `scoring_result.docx` 파일에서 채점 결과를 확인할 수 있습니다.<br><br>
![스크린샷 2024-05-22 155630](https://github.com/kim-nini/aws-ccp-exam-prep/assets/144877020/490e59d0-8282-40cb-af03-0337360f519a)


## 의존성

`pom.xml` 파일에 필요한 의존성들이 포함되어 있습니다:

```xml
<dependencies>
    <!-- Apache POI -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.2</version>
    </dependency>
    <!-- Log4j2 -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.17.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.17.0</version>
    </dependency>
</dependencies>

