CREATE TABLE user(
    uid VARCHAR(12) NOT NULL,
    name VARCHAR(25) NOT NULL,
    age INT NOT NULL,
    occupation VARCHAR(20) NOT NULL,
    ucid INT NOT NULL,
    PRIMARY KEY (uid),
    FOREIGN KEY (ucid) REFERENCES user_category(ucid)
);

CREATE TRIGGER user_insert_domain BEFORE INSERT ON user
for each row
begin
    if not (new.ucid >= 0 and new.ucid <= 9 and LENGTH(new.uid) = 12 and new.age >= 10 and new.age <= 99) then
        signal sqlstate '45000' set message_text = 'domain error new.ucid >= 0 and new.ucid <= 9 and LENGTH(new.uid) = 12 and new.age >= 10 or new.age <= 99';
    end if;
end;

CREATE TRIGGER user_update_domain BEFORE UPDATE ON user
for each row
begin
    if not (new.ucid >= 0 and new.ucid <= 9 and LENGTH(new.uid) = 12 and new.age >= 10 and new.age <= 99) then
        signal sqlstate '45000' set message_text = 'domain error new.ucid >= 0 and new.ucid <= 9 and LENGTH(new.uid) = 12 and new.age >= 10 or new.age <= 99';
    end if;
end;
