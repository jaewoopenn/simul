'''
Created on 2016. 9. 30.

@author: jaewo
'''
def main():
    i_f = open("C:/Users/jaewoo/data/file/test.txt","r")
    for line in i_f:
        val=line.strip()
        print val
if __name__ == '__main__':
    main()