# \#rasp_proj

## Central Server

__function(Service)__

1. __자리관리(사용선택 및 현황 표시)(가능하면 자바 스크립트)__
	* 현황 표시 : 빈 자리 표시
	* 사용선택 : 현황판 업데이트 및 나갈 때 사용종료

2. __센서 값 처리(Local Server의 통지)__  
	* Local Server의 통지를 받으면 알맞은 처리
	* 통지(상태) : 온도 초과, 온도 미만, 습도 초과, 습도 미만, 적정 온도, 적정 습도
	* 상태에 따른 처리 :
	* 온도 초과 : 에어컨 가동
	* 온도 미만 : 히터 가동
	* 적정 온도(초과->적정, 미만->적정) : 에어컨, 히터 중지
	* 습도 초과 : 제습기 가동
	* 습도 미만 : 가습기 가동
	* 적정 습도(초과->적정, 미만->적정) : 가습기, 제습기 중지
***
## Local Server(방 기준)

__function(Service)__

1. __센서 값 감시(데시벨, 온도, 습도)__
	* 데시벨 : 설정 데시벨 초과 일 경우 2.과정 실행
	* 온도, 습도 : 설정 온·습도(+- x.오차범위) 초과, 미만 일 경우 Central Server에 통지

2. __센서 값 처리(데시벨)__
	* 설정 데시벨 초과시 스크린에 해당 자리번호+"메시지" 전달
