CREATE TABLE rent(
    uid VARCHAR(12) NOT NULL,
    callnum VARCHAR(8) NOT NULL,
    copynum INT NOT NULL,
    checkout DATE NOT NULL,
    return_date DATE ,
    PRIMARY KEY (uid, callnum, copynum, checkout),
    FOREIGN KEY (uid) REFERENCES user(uid),
    FOREIGN KEY (callnum) REFERENCES car(callnum)
);

CREATE TRIGGER rent_insert_domain BEFORE INSERT ON rent
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.copynum >= 1 AND new.copynum <= 9 AND LENGTH(new.uid) = 12) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.copynum >= 1 AND new.copynum <= 9 AND LENGTH(new.uid) = 12';
    end if;
end;

CREATE TRIGGER rent_update_domain BEFORE UPDATE ON rent
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.copynum >= 1 AND new.copynum <= 9 AND LENGTH(new.uid) = 12) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.copynum >= 1 AND new.copynum <= 9 AND LENGTH(new.uid) = 12';
    end if;
end;