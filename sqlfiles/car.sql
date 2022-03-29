CREATE TABLE car(
    callnum VARCHAR(8) NOT NULL,
    name VARCHAR(10) NOT NULL,
    manufacture DATE NOT NULL,
    time_rent INT NOT NULL,
    ccid INT NOT NULL,
    PRIMARY KEY (callnum),
    FOREIGN KEY (ccid) REFERENCES car_category(ccid)
);

CREATE TRIGGER car_insert_domain BEFORE INSERT ON car
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.time_rent >= 10 AND new.time_rent <= 99 AND new.ccid >= 0 AND new.ccid <= 9) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.time_rent >= 10 AND new.time_rent <= 99 AND new.ccid >= 0 AND new.ccid <= 9';
    end if;
end;

CREATE TRIGGER car_update_domain BEFORE UPDATE ON car
for each row
begin
    if not (LENGTH(new.callnum) = 8 AND new.time_rent >= 10 AND new.time_rent <= 99 AND new.ccid >= 0 AND new.ccid <= 9) then
        signal sqlstate '45000' set message_text = 'LENGTH(new.callnum) = 8 AND new.time_rent >= 10 AND new.time_rent <= 99 AND new.ccid >= 0 AND new.ccid <= 9';
    end if;
end;