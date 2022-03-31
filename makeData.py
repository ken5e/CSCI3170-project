with open("./data/user_category.txt", "w") as f:
    for i in range(1, 10):
        f.write(f"{i}\t{i}\t{i}\n")

with open("./data/car.txt", "w") as f:
    for i in range(1, 7):
        f.write(f"1234567{i}\t{i}\tAE86{i}\tTOYOTA{i}\t2022-03-2{i}\t1{i}\t{i}\n")

with open("./data/rent.txt", "w") as f:
    for i in range(1, 4):
        f.write(f"1234567{i}\t{i}\ts123456789{i}{i}\t2022-01-1{i}\t2022-01-1{i + 1}\n")

with open("./data/car_category.txt", "w") as f:
    for i in range(9):
        f.write(f"{i}\tvan{i}\n")

with open("./data/user.txt", "w") as f:
    for i in range(1, 8):
        f.write(f"s123456789{i}{i}\tPaul{i}\t2{i}\tdriver{i}\t{i}\n")