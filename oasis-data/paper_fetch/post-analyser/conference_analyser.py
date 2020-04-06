import re


def is_times(str):
    times_dic = {
        'First',
        'Second',
        'Third',
        'Fourth',
        'Fifth',
        'Sixth',
        'Seventh',
        'Eighth',
        'Ninth',
        'Tenth',
        'Eleventh',
        'Twelfth',
        'Thirteenth',
        'Fourteenth',
        'Fifteenth',
        'Sixteenth',
        'Seventeenth',
        'Eighteenth',
        'Nineteenth',
        'Twentieth',
    }
    ieee_dic = {
        'IEEE',
        'ACE',
        'IEEE/ACE',
        'IEEE/ACM',
        'ACM/IEEE',
        'ASE'
    }
    return not (str in times_dic)


def inc_Num(word):
    res = re.match('.*[0-9]+.*', word)
    if res:
        return False
    else:
        return True
    # return res


def subHigh(line):
    temp = ' ' + line
    res = re.sub('[ |(][0-9A-Z/\']{3,100}[ |)|\n]', ' ', temp)
    return res


def filter_times(line):
    strs = list(filter(is_times, line.split(' ')))
    res = ' '.join(strs)
    return res


def filter_num(line):
    strs = list(filter(inc_Num, line.split(' ')))
    res = ' '.join(strs)
    return res


def sub_proceedings(line):
    res0 = re.sub('Proceedings\.|'
                  'Proceedings of the Annual |'
                  'Proceedings of the|'
                  'Proceedings of|'
                  ', Proceedings|'
                  '\[*Proceedings]',
                  '', line)
    res = re.sub('Proceedings *', '', res0)
    return res


def sub_kuohao(line):
    res = re.sub('\(.*[)]*', '', line)
    nres = re.sub('\[.*\]', '', res)
    return nres


def sub_end_High(line):
    res = re.sub('\. [A-Z]*$', ' ', line)
    res = re.sub(', [A-Z]*$', ' ', res)
    res = re.sub('- [A-Z]*$', ' ', res)
    res = re.sub(', Part.*$', ' ', res)
    return res


def sub_end(line):
    res = line.rstrip().rstrip(' ,.-')
    return res + '\n'


def sub_start(line):
    res = line.lstrip(' .-')
    return res


def conference_filter(line):
    res0 = filter_times(line)
    res1 = filter_num(res0)
    res2 = sub_proceedings(res1)
    res3 = sub_end_High(res2)
    res = sub_start(sub_end(sub_kuohao(res3)))
    t = re.sub(r'[()*.]', "", res)
    return t


if __name__ == '__main__':
    files = ['conferences.txt', 'conferences_post.txt']

    f = open(files[0])
    w = open(files[-1], 'w')
    raw_data = []
    line = f.readline()
    while line:
        raw_data.append(line)
        line = f.readline()

    res_set = set()
    res_list = []
    i = 0
    for line in raw_data:
        res0 = filter_times(line)
        res1 = filter_num(res0)
        res2 = sub_proceedings(res1)
        res3 = sub_end_High(res2)
        res = sub_start(sub_end(sub_kuohao(res3)))
        t = re.sub(r'[()*.]', "", res)
        w.write(t)
