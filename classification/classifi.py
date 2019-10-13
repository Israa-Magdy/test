import xlrd
import operator
import math

class Node:
    item=str()
    frequence=int()
    prop=float()

    def __init__(self,item):
        self.item = item
        self.frequence = 1
        self.prop = 0.0
    def Get_prop(self):
        return self.prop
    def Get_frequence(self):
        return self.frequence

table = list()
test = list()
ClassLabels=list()
predictionClassLabel=list()

def readfile(table,test):
         workbook = xlrd.open_workbook("car.xls")
         worksheet = workbook.sheet_by_index(0)
         for row in range(1, worksheet.nrows):
              temp =list()
              for column in range(1, worksheet.ncols):
                  temp.append(worksheet.cell_value(row, column))
              if row%4 == 0:
                  test.append(temp)
                  continue
              table.append(temp)


def getClassLable():
    obj1 = Node("unacc")
    obj2 = Node("acc")
    obj3 = Node("vgood")
    obj4 = Node("good")
    ClassLabels.append(obj1)
    ClassLabels.append(obj2)
    ClassLabels.append(obj3)
    ClassLabels.append(obj4)
    for i in table:
        for j in ClassLabels:
            if i[6]== j.item:
                j.frequence+=1
    for i in ClassLabels:
        i.prop=i.frequence/len(table)

    for i in ClassLabels:
        print(i.item,"    ",i.frequence,"    ",i.prop)


def getfrequence(item1,item2,index):
    counter=0
    for i in table:
        if item1 == i[6]:
            if item2 == i[index]:
                counter+=1
    return counter


def testingdata():
    for i in test:
        rowprop=0.0
        pointer = ClassLabels[0].item
        for j in ClassLabels :
            prob = j.prop
            for cell in range(0,6):
                x = getfrequence(j.item , i[cell] , cell)
                if x == 0:
                    x=1
                prob *= ( x/j.frequence )
            if prob > rowprop:
                rowprop = prob
                pointer = j.item
        predictionClassLabel.append(pointer)



def euclideanDistance(point1 , point2 , length):
    distance = 0
    for x in range(length):
        distance += pow((point1[x] - point2[x]) , 2)
    return math.sqrt(distance)

def getNeighbors(training , test , k=5):
    predictionClassLabel.clear()
    for i in test:
        distances = list()
        for j in training:
            dist = euclideanDistance(i,j,len(i)-1)
            object = Node(j[6])
            object.prop=dist
            distances.append(object)
        distances.sort(key=Node.Get_prop)
        a=0
        while a < k :
            h=a+1
            while h< k:
                if distances[a].item == distances[h].item:
                    distances[a].frequence+=1
                h+=1
            a+=1
        distances.sort(key=Node.Get_frequence ,reverse=True)
        predictionClassLabel.append(distances[0].item)

def getAccuracy(testSet, predictions):
    correct = 0
    incorrect=0
    for x in range(len(testSet)):
        if testSet[x][6] == predictions[x]:
            correct += 1
        else:
            incorrect += 1
    print("Correct : ",correct, "incorrect : ", incorrect)
    return correct


############################################Main#####################################################################################################

readfile(table,test)
getClassLable()
testingdata()
Correct=getAccuracy(test, predictionClassLabel)
print("The Accuracy : ", Correct/len(test)*100)
getNeighbors(table , test )
Correct=getAccuracy(test, predictionClassLabel)
print("The Accuracy : ", Correct/len(test)*100)