CREATE TABLE user_category(
    ucid INT NOT NULL,
    max INT NOT NULL,
    period INT NOT NULL,
    PRIMARY KEY (ucid)
);

CREATE TRIGGER user_cat_insert_domain BEFORE INSERT ON user_category
for each row
begin
    if new.ucid < 0 or new.ucid > 9 or new.max < 0 or new.max > 9 or new.period < 0 or new.period > 99 then
        signal sqlstate '45000' set message_text = 'domain error 0 <= ucid AND ucid <= 9 AND max <= 0 AND max <= 9 AND period <= 0  AND period <= 99';
    end if;
end;


CREATE TRIGGER user_cat_update_domain BEFORE UPDATE ON user_category
for each row
begin
    if new.ucid < 0 or new.ucid > 9 or new.max < 0 or new.max > 9 or new.period < 0 or new.period > 99 then
        signal sqlstate '45000' set message_text = 'domain error 0 <= ucid AND ucid <= 9 AND max <= 0 AND max <= 9 AND period <= 0  AND period <= 99';
    end if;
end;