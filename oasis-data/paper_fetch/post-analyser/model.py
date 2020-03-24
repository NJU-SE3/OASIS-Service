file_names = ['../affiliations.txt', '../conferences.txt']


# 机构处理
def affiliation_handler(file):
    with open(file) as f:
        for line in f:
            import re
            t = re.sub(r'[().*]', "", re.sub(r'\(.*\)', "", line.strip()).strip())
            print(t.strip())



affiliation_handler(file_names[0])