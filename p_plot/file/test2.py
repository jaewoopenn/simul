'''
Created on 2015. 12. 17.

@author: jaewo
'''
import file.MFile as mf


def select():
    path2()

def path1():
    lst=mf.load_fn("test/test_word.txt")
    for m in lst:
        print(m)
    
def path2():
    lst=mf.load_fn("test/test_word.txt")
    for m in lst:
        wd=m.split()
        if wd[0]=='s':
            print("st   ",wd[1],wd[2])
        elif wd[0]=='d':
            print("diag ",wd[1],wd[2])
        elif wd[0]=='h':
            print("hori ",wd[1])
            
        else:
            print(wd)
    


def main():
    select()

if __name__ == '__main__':
    main()