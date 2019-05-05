'''
Created on 2015. 12. 17.

@author: jaewo
'''
import file.MFile as mf

class gl:
#     path="d:/data/"
    path="/data/"

def main():
    x=mf.load(gl.path+"test/test.txt")
#     print("hihi")
    print(x)

if __name__ == '__main__':
    main()