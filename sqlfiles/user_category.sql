CREATE TABLE user_category(
    ucid INT NOT NULL,
    max INT NOT NULL,
    period INT NOT NULL,
    PRIMARY KEY (ucid)
);

CREATE TRIGGER user_cat_insert_domain BEFORE INSERT ON user_category
for each row
begin
    if not (new.ucid >= 0 and new.ucid <= 9 and new.max >= 1 and new.max <= 9 and new.period >= 0 and new.period <= 99) then
        signal sqlstate '45000' set message_text = 'domain error new.ucid >= 0 and new.ucid <= 9 and new.max >= 1 and new.max <= 9 and new.period >= 0 and new.period <= 99';
    end if;
end;


CREATE TRIGGER user_cat_update_domain BEFORE UPDATE ON user_category
for each row
begin
    if not (new.ucid >= 0 and new.ucid <= 9 and new.max >= 1 and new.max <= 9 and new.period >= 0 and new.period <= 99) then
        signal sqlstate '45000' set message_text = 'new.ucid >= 0 and new.ucid <= 9 and new.max >= 1 and new.max <= 9 and new.period >= 0 and new.period <= 99';
    end if;
end;