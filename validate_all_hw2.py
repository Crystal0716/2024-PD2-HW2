#!/bin/python3

import subprocess
import os
import sys
import shutil


base_path = "/home/share"
#base_path = "/hw_judge"

TESTCASES = [
    f"{base_path}/hw2/tc0",
    f"{base_path}/hw2/tc1",
    f"{base_path}/hw2/tc2",
    f"{base_path}/hw2/tc3",
    f"{base_path}/hw2/tc4",
    f"{base_path}/hw2/tc5",
    f"{base_path}/hw2/tc6",
    f"{base_path}/hw2/tc7",
    f"{base_path}/hw2/tc8",
    f"{base_path}/hw2/tc9",
    f"{base_path}/hw2/tc10",
    f"{base_path}/hw2/tc11",
    f"{base_path}/hw2/tc12",
]

ANSWERS = [
    [f"{base_path}/hw2/ans0/MyClass.java"],
    [f"{base_path}/hw2/ans1/Student.java"],
    [f"{base_path}/hw2/ans2/Car.java"],
    [f"{base_path}/hw2/ans3/Car.java", f"{base_path}/hw2/ans3/Wheel.java"],
    [f"{base_path}/hw2/ans4/Calculator.java", f"{base_path}/hw2/ans4/User.java"],
    [f"{base_path}/hw2/ans5/Person.java", f"{base_path}/hw2/ans5/Student.java", f"{base_path}/hw2/ans5/Teacher.java"],
    [f"{base_path}/hw2/ans6/Employee.java"],
    [f"{base_path}/hw2/ans7/Movie.java", f"{base_path}/hw2/ans7/Director.java", f"{base_path}/hw2/ans7/Actor.java"],
    [f"{base_path}/hw2/ans8/LibraryBook.java", f"{base_path}/hw2/ans8/BorrowRecord.java", f"{base_path}/hw2/ans8/Reader.java"],
    [f"{base_path}/hw2/ans9/MusicPlayer.java", f"{base_path}/hw2/ans9/Song.java", f"{base_path}/hw2/ans9/Artist.java"],
    [f"{base_path}/hw2/ans10/Product.java", f"{base_path}/hw2/ans10/ShoppingCart.java", f"{base_path}/hw2/ans10/Payment.java"],
    [f"{base_path}/hw2/ans11/Company.java", f"{base_path}/hw2/ans11/Employee.java", f"{base_path}/hw2/ans11/Department.java"],
    [f"{base_path}/hw2/ans12/ElectronicDevice.java", f"{base_path}/hw2/ans12/Smartphone.java", f"{base_path}/hw2/ans12/Technician.java"]
]

DIFF_DIR = os.path.join('.', 'diff')
code_generator_path = os.path.expanduser("~/dam2/CodeGenerator.java")
code_generator_class = os.path.expanduser("CodeGenerator")
# 檢查是否存在 CodeGenerator.java 檔案，並且路徑正確
if not os.path.exists(code_generator_path):
    print("❌錯誤：CodeGenerator.java 文件不存在或路徑錯誤!")
    print("請確認 CodeGenerator.java 是否存在於 ~/hw2/ 目錄下!")
    exit(1)

# 編譯 CodeGenerator.java
if subprocess.call(["javac", code_generator_path]) == 0:
    print("編譯完成")
else:
    print("❌錯誤：編譯時發生錯誤")
    exit(1)

# 執行測試案例
def run_testcase(tc_number, testcase, answer_files):
    try:
        # 先清空之前產生過的檔案
        for answer in answer_files:
            file_name = os.path.basename(answer)
            if os.path.exists(file_name):
                subprocess.run(["rm", file_name])
        subprocess.run(["java", code_generator_class, testcase], timeout=60)
        for answer in answer_files:
            file_name = os.path.basename(answer)
            if not os.path.exists(file_name):
                print(f"❌找不到 {file_name}")
                continue
            diff_process = subprocess.run(["sdiff", "--ignore-trailing-space", "-w", "120", "-l", file_name, answer], text=True, capture_output=True)
            if diff_process.returncode == 0:
                print(f"{file_name}: ✅")
            else:
                # Save output of diff to file
                diff_log = os.path.join(f'diff/testcase{tc_number}', answer.split('/')[-1] + "_diff")
                if not os.path.exists(os.path.dirname(diff_log)):
                    os.makedirs(os.path.dirname(diff_log))
                with open(diff_log, 'w') as f:
                    f.write(diff_process.stdout)
                print(f"{file_name}❌\t| 結果已存至 '{diff_log}'")
    except FileNotFoundError as e:
        print("找不到 sdiff 命令")
    except subprocess.TimeoutExpired:
        print("❌執行超時")
        sys.exit(1)  # 終止程式


if __name__ == '__main__':
    # Remove files in DIFF_DIR
    if os.path.exists(DIFF_DIR):
        shutil.rmtree(DIFF_DIR)
    os.mkdir(DIFF_DIR)
    
    # 如果沒有傳遞參數，預設執行所有測試案例
    if len(sys.argv) == 1:
        for i, (testcase, answer) in enumerate(zip(TESTCASES, ANSWERS)):
            print(f"testcase{i}: \n", end='')
            run_testcase(i, testcase, answer)
    else:
        # 如果傳遞了參數，只執行指定編號的測試案例
        testcase_number = int(sys.argv[1])
        if 0 <= testcase_number < len(TESTCASES):
            print(f"testcase{testcase_number}: ", end='')
            run_testcase(testcase_number, TESTCASES[testcase_number], ANSWERS[testcase_number])
        else:
            print("❌錯誤：無效的測試案例編號")
            exit(1)
    
