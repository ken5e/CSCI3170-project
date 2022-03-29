CREATE TABLE car_category(
    ccid INT NOT NULL,
    ccname VARCHAR(20) NOT NULL,
    PRIMARY KEY (ccid)
);

CREATE TRIGGER car_category_insert_domain BEFORE INSERT ON car_category
for each row
begin
    if not (new.ccid >= 0 and new.ccid <= 9) then
        signal sqlstate '45000' set message_text = 'new.ccid >= 0 and new.ccid <= 9';
    end if;
end;

CREATE TRIGGER car_category_update_domain BEFORE UPDATE ON car_category
for each row
begin
    if not (new.ccid >= 0 and new.ccid <= 9) then
        signal sqlstate '45000' set message_text = 'new.ccid >= 0 and new.ccid <= 9';
    end if;
end;