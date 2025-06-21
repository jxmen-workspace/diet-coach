# diet-coach

ToDo

- [x] 이미지 파일 업로드 -> 탄단지 / 칼로리 분석
    - [x] spring ai 의존성 추가
    - [x] string template 만들기 (이미지의 탄단지/칼로리를 분석하시오.)
    - [x] rest api 개발 (이미지 업로드가 가능해야 함, 우선 이미지 입력만)
      ```mermaid
      sequenceDiagram
        User ->> Web: 이미지 업로드
        Web ->> Server: POST /api/analyze (이미지)
        Server ->> AI: 이미지 분석 요청
        AI -->> Server: 분석 결과 (탄단지/칼로리)
        Server -->> Web: 분석 결과 JSON
        Web -->> User: 결과 표시
      ```
