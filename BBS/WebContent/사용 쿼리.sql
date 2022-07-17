USER 테이블 생성
    CREATE TABLE USER (
        userID VARCHAR(20),
        userPassword VARCHAR(20),
        userName VARCHAR(20),
        userGender VARCHAR(20),
        userEmail VARCHAR(50),
        createdDate DATETIME,
        modifiedDate DATETIME,
        PRIMARY KEY (userID)
        )DEFAULT CHARSET=utf8;


BOARD 테이블 생성
    CREATE TABLE BOARD (
        board_ID INT,
        board_Title VARCHAR(50),
        board_Contents VARCHAR(2048),
        userID VARCHAR(20),
        board_createdDate DATETIME,
        board_modifieID VARCHAR(20),
        board_modifiedDate DATETIME,
        bbsAvailable INT,
        PRIMARY KEY (board_ID)
        )DEFAULT CHARSET=utf8;