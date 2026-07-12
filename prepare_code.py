import os
import re

# تنظیمات
EXTENSIONS = {'.java'}
IGNORE_DIRS = {'.git', '.idea', 'target', 'build', 'out', '.gradle'}
OUTPUT_FILE = 'my_project_code_compact.txt'

def clean_java_code(code):
    # ۱. حذف کامنت‌های چندخطی /* ... */
    code = re.sub(r'/\*.*?\*/', '', code, flags=re.DOTALL)
    
    # ۲. حذف کامنت‌های تک‌خطی //
    code = re.sub(r'//.*', '', code)
    
    cleaned_lines = []
    for line in code.splitlines():
        # ۳. حذف فضاهای خالی از ابتدا و انتهای خط برای بررسی محتوا
        stripped_line = line.strip()
        
        # ۴. فقط خطوطی که خالی نیستند را اضافه کن
        if stripped_line:
            # اینجا خود خط اصلی (با رعایت تورفتگی) اضافه می‌شود
            cleaned_lines.append(line.rstrip())
            
    return "\n".join(cleaned_lines)

def generate_code_file():
    total_files = 0
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as outfile:
        for root, dirs, files in os.walk('.'):
            # نادیده گرفتن پوشه‌های غیرضروری
            dirs[:] = [d for d in dirs if d not in IGNORE_DIRS]
            
            for file in files:
                if any(file.endswith(ext) for ext in EXTENSIONS):
                    # نادیده گرفتن خودِ فایل خروجی اگر در همین مسیر باشد
                    if file == OUTPUT_FILE:
                        continue
                        
                    file_path = os.path.join(root, file)
                    
                    try:
                        with open(file_path, 'r', encoding='utf-8') as infile:
                            raw_code = infile.read()
                            
                        # فشرده‌سازی کد
                        compact_code = clean_java_code(raw_code)
                        
                        # نوشتن در فایل با هدر بسیار کوتاه برای صرفه‌جویی در فضا
                        outfile.write(f"--- FILE: {file_path} ---\n")
                        outfile.write(compact_code)
                        outfile.write("\n\n")
                        total_files += 1
                        
                    except Exception as e:
                        print(f"Could not read file {file_path}: {e}")

    print(f"عملیات با موفقیت انجام شد!")
    print(f"تعداد {total_files} فایل جاوا فشرده‌سازی و در '{OUTPUT_FILE}' ذخیره شدند.")

if __name__ == "__main__":
    generate_code_file()
