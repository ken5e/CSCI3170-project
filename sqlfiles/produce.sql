CREATE TABLE produce(
    cname VARCHAR(10),
    callnum VARCHAR(8),
    PRIMARY KEY (cname, callnum),
    FOREIGN KEY (callnum) REFERENCES car(callnum)
);

CREATE TRIGGER produce_insert_domain BEFORE INSERT ON produce
for each row 
begin
    if not (LENGTH(new.callnum) = 8) then
        signal sqlstate '45000' set message_text = 'LENGTH(callnum) = 8';
    end if;
end;

CREATE TRIGGER produce_update_domain BEFORE UPDATE ON produce
for each row 
begin
    if not (LENGTH(callnum) = 8) then
        signal sqlstate '45000' set message_text = 'LENGTH(callnum) = 8';
    end if;
end;
