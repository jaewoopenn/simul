'''
Created on 2015. 5. 26.

@author: Jaewoo Lee
'''
import Simul

'''
Task info
'''

def getTasks():
    task_list=[[4,1],[3,2]]
#     task_list=[[4,2],[3,2]]
    return task_list

'''
Common
'''

def main():
    task_list=getTasks()
    Simul.prepare(task_list)
    Simul.run()
    #test()
    print "end"


if __name__ == '__main__':
    main()

