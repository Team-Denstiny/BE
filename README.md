# BE
Denstiny - 백엔드 repo

# 유다시티 Git 커밋메시지 스타일가이드
 - 원본: https://udacity.github.io/git-styleguide/

## 메시지 구조
```
type: subject

body

footer

```

## 커밋 예시
feat: 회원 가입 기능 구현

SMS, 이메일 중복확인 API 개발

Resolves: #123
Ref: #456
Related to: #48, #45

## type
각 커밋의 유형에 맞게 아래의 키워드 중 하나를 선택
 - feat: 새로운 기능
 - fix: 버그 수정
 - docs: 문서 변경
 - style: EOL, 세미콜론 등 코드에 대한 변경이 아닌 컨벤션에 관련된 변경
 - refactor: 프로덕션 코드 리팩토링
 - test: 테스트 추가, 리팩토링 테스트 ( 프로덕션 코드 변경 없음 )
 - chore: 빌드, 릴리즈, 설정 등의 변경 ( 프로덕션 코드 변경 없음 )

## subject
제목은 50글자를 넘지 않아야 하며 대문자로 시작하고 마침표로 끝나지 않아야 함.
무엇을 햇는지 보다 커밋이 무엇인 지 설명

## body
(선택사항) 커밋에 설명이 필요할 때 사용하며 제목과 본문 사이에는 한 줄을 비워야 한다.
한 줄은 72자 이내로 작성해야 하며 문단으로 나누거나 불렛을 사용하여 내용을 구분한다.

## footer
(선택사항) 이슈 트래커의 ID를 참조할 때 사용

```
Resolves: #123
See also: #456, #789
```
