'''
Created on 2015. 12. 17.

@author: jaewo
'''
import util.MFile as mf

def main():
    x=mf.load("/data/test/test.txt")
#     print("hihi")
    print(x)

if __name__ == '__main__':
    main()