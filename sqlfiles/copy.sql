CREATE TABLE copy(
    callnum VARCHAR(8) NOT NULL,
    copynum INT NOT NULL,
    PRIMARY KEY (callnum, copynum),
    FOREIGN KEY (callnum) REFERENCES car(callnum)
);

CREATE TRIGGER copy_insert_domain BEFORE INSERT ON copy
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.copynum >= 0 AND new.copynum <= 9) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.copynum >= 0 AND new.copynum <= 9';
    end if;
end;


CREATE TRIGGER copy_update_domain BEFORE UPDATE ON copy
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.copynum >= 0 AND new.copynum <= 9) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.copynum >= 0 AND new.copynum <= 9';
    end if;
end;