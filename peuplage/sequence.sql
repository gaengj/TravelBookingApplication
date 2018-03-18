DROP SEQUENCE ClientKey;
DROP SEQUENCE SimuKey;
DROP SEQUENCE ResaKey;
DROP SEQUENCE ResaHotelKey;


CREATE Sequence ClientKey;
CREATE Sequence SimuKey;
CREATE Sequence ResaKey;
CREATE Sequence ResaHotelKey;

SELECT SimuKey.nextval, ClientKey.nextval, ResaKey.nextval, ResaHotelKey.nextval from DUAL;
