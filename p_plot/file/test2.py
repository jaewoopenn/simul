'''
Created on 2015. 12. 17.

@author: jaewo
'''
import file.MFile as mf


def select():
    path1()

def path1():
    x=mf.load(gl.path+"test/test_word.txt")
#     print("hihi")
    print(x)
    
    

class gl:
#     path="d:/data/"
    path="/data/"

def main():
    select()

if __name__ == '__main__':
    main()