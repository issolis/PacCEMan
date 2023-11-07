#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/*---------------PathFinding Data Structures--------------------*/
struct node
{
    struct node *up;
    struct node *down;
    struct node *right;
    struct node *left;
    struct node *parent; 
    int id;
    int f; 
    int h; 
    int g; 

} *head;

struct nodeList
{
    struct node *node; 
    struct nodeList *next;
} *openListHead, *closeListHead; 

struct nodeId{
    int id; 
    struct nodeId *next; 
}*IdHead;

struct node* getNodeById(int id){
    struct node *currentRow = head;
    while(currentRow!=NULL){
        struct node *auxiliar=currentRow;
        while(auxiliar !=NULL){
            
            if (auxiliar->id==id) return auxiliar; 
            auxiliar=auxiliar->right; 
        }
        currentRow=currentRow->down; 
    } 
    return currentRow; 
}

struct node* getMinNode(struct nodeList *list){
    struct nodeList *current = list;
    struct node *nodeMin = list->node; 
    while (current->next!=NULL){
        if(nodeMin->f > current->next->node->f)
            nodeMin = current->next->node;
        current=current->next; 
    } 
    return nodeMin; 
}

void addNodeIdList (int id){
    struct nodeId *newNode = (struct nodeId *)malloc(sizeof(struct nodeId));
    newNode->next = NULL; 
    newNode->id=id;
    if (IdHead->id==-1)
        IdHead=newNode; 
    else{
        newNode->next = IdHead; 
        IdHead = newNode; 
    }
}

void addNodeCloseList (struct node *node){
    struct nodeList *newNode = (struct nodeList *)malloc(sizeof(struct nodeList));
    newNode->next=NULL; 
    newNode->node=node;     
    if (closeListHead->node==NULL){
        closeListHead  = newNode; 
    }
    else{
        newNode->next = closeListHead;
        closeListHead = newNode;
    }
}

void addNodeOpenList ( struct node *node){
    struct nodeList *newNode = (struct nodeList *)malloc(sizeof(struct nodeList));
    newNode->next=NULL; 
    newNode->node=node;     
    if (openListHead->node==NULL){
        openListHead  = newNode; 
    }
    else{
        newNode->next = openListHead;
        openListHead = newNode;
    }
}

void deleteNodeOpenList (struct node *node){
    struct nodeList *current = openListHead; 
    struct nodeList *before  = openListHead; 
    while  (current!=NULL){
        if (current->node==node){
            
            if (current == openListHead)
                openListHead = current->next; 
            
            else{
                before->next=current->next;
            }
            break; 
        }
        before = current; 
        current = current->next; 
    }
}

bool member (struct nodeList *listHead, struct node *node){
    struct nodeList *current = listHead; 
    while  (current!=NULL){
        if (current->node==node)
            return true; 
        current = current->next; 
    }    
    return false;
}

void showIdList (){
    struct nodeId *current = IdHead; 
    while (current!=NULL){
        printf("%i ", current->id); 
        current = current->next;
    }
}
/*-----------------------------------------------------------------------*/

void madeMatrix(int raw, int colum)
{
    
    struct node *auxiliar  = head; 
    struct node *auxiliar1 = head; 
    struct node *auxiliar2 = head; 
    for(int i = 1; i <= raw ; i++){ 
        auxiliar1=auxiliar; 
        for (int j = 1; j < colum; j++)
        {
            struct node *newNode = (struct node *)malloc(sizeof(struct node));

            if(i==1){ 
                auxiliar->right=newNode;
                newNode->left  = auxiliar;
                newNode->right = NULL; 
                newNode->down=NULL;
                newNode->up=NULL; 
                newNode->id = j+(i-1)*raw;

                auxiliar= newNode;
            }
            else{
                auxiliar->right=newNode;
                newNode->left  = auxiliar;
                newNode->right = NULL; 
                newNode->down=NULL;
                newNode->up=auxiliar2;
                auxiliar2->down=newNode; 
                newNode->id = j+(i-1)*raw;

                auxiliar= newNode;
                auxiliar2=auxiliar2->right;
            }
        }
        if (i<raw){
        struct node *newDownNode = (struct node *)malloc(sizeof(struct node));
        auxiliar1->down=newDownNode; 

        newDownNode->id=i*raw; 
        newDownNode->left=NULL; 
        newDownNode->right=NULL; 
        newDownNode->down=NULL; 
        newDownNode->up=auxiliar1;

        auxiliar=newDownNode;   
        
        auxiliar2=auxiliar1->right;
        auxiliar1=auxiliar; 
        }
        
    }

    
}    

void printMatrix() {
    struct node *currentRow = head;
    while(currentRow!=NULL){
        struct node *auxiliar=currentRow;
        currentRow=currentRow->down; 
        while(auxiliar!=NULL){
            struct node *temp = auxiliar;
            printf("%i ", auxiliar->id);
            auxiliar=auxiliar->right; 
            free(temp); 
        }
        printf("\n");
    }    
    head=NULL;
    free(head); 
}

int convertX(int id){
    return id%8; 
}

int convertY(int id){
    return id/8; 
}

void defineH(int FinalNodeId){
    struct node *currentRow = head;
    int posXFinalNode = convertX(FinalNodeId); 
    int posYFinalNode = convertY(FinalNodeId); 
    while(currentRow!=NULL){
        struct node *auxiliar=currentRow;
        while(auxiliar!=NULL){
            struct node *temp = auxiliar;
            int auxiliarY=convertY(auxiliar->id);
            int auxiliarX=convertX(auxiliar->id);
            auxiliar->h= abs(posYFinalNode-auxiliarY)+abs(posXFinalNode-auxiliarX);
            auxiliar->f=auxiliar->h; 
            auxiliar->g=0; 
            auxiliar=auxiliar->right; 
        }
        currentRow=currentRow->down; 
    }    
 
}

void showRoute(struct node *finalNode){
    struct node *current = finalNode;
    while (current!=NULL){
        addNodeIdList(current->id);
        current=current->parent; 
    }
    showIdList();
}

void blockNodes (){
     int matriz[8][8] = {
    {0,0,0,0,0,0,0,0},
    {0,0,0,1,0,0,0,0},
    {0,0,0,1,0,0,0,0},
    {0,1,1,1,0,0,0,0},
    {0,1,0,1,1,1,0,0},
    {0,0,0,0,0,0,0,0},
    {0,0,0,0,1,1,0,0},
    {0,0,0,0,0,0,0,0},
    };
    for (int i=0; i<8; i++){
        for (int j=0; j<8; j++){
            if (matriz[i][j]==1)
                addNodeCloseList(getNodeById(i*8+j));
        }
    }
}


void pathfinding(int inicialNodeId, int finalNodeId){

    defineH(finalNodeId);
    struct node *minNode = getNodeById(inicialNodeId); 
    struct node *finalNode = getNodeById(finalNodeId);
    addNodeCloseList(minNode);  
    minNode->f = minNode->h; 
    
    while (true){

        if (minNode->right!= NULL && !member(closeListHead, minNode->right)){
            if (minNode->right->g==0){
                minNode->right->g = 1 + minNode->g;
                minNode->right->f = minNode->right->g + minNode->right->h;
                minNode->right->parent = minNode;  
                addNodeOpenList(minNode->right);
            }
            else{
                if (minNode->g + 1 < minNode->right->g){
                    minNode->right->g = 1 + minNode->g;
                    minNode->right->f = minNode->right->g +minNode->right->h;
                    minNode->right->parent = minNode; 
                    addNodeOpenList(minNode->right);
                }
            }
            
        }
        if (minNode->left!= NULL && !member(closeListHead, minNode->left)){
            if (minNode->left->g==0){
                minNode->left->g = 1 + minNode->g;
                minNode->left->f = minNode->left->g + minNode->left->h;
                minNode->left->parent = minNode;  
                addNodeOpenList(minNode->left);
            }
            else{
                if (minNode->g + 1 < minNode->left->g){
                    minNode->left->g = 1 + minNode->g;
                    minNode->left->f = minNode->left->g + minNode->left->h;
                    minNode->left->parent = minNode;  
                    addNodeOpenList(minNode->left);
                }
            } 
        }
        if (minNode->up!= NULL && !member(closeListHead, minNode->up)){
            if (minNode->up->g==0){
                minNode->up->g = 1 + minNode->g;
                minNode->up->f = minNode->up->g + minNode->up->h;
                minNode->up->parent = minNode;  
                addNodeOpenList(minNode->up);
            }
            else{
                if (minNode->g + 1 < minNode->up->g){
                    minNode->up->g = 1 + minNode->g;
                    minNode->up->f = minNode->up->g + minNode->up->h;
                    minNode->up->parent = minNode;  
                    addNodeOpenList(minNode->up);
                }
            } 
        }
        if (minNode->down!= NULL && !member(closeListHead, minNode->down)){
            if (minNode->down->g==0){
                minNode->down->g = 1 + minNode->g;
                minNode->down->f = minNode->down->g + minNode->down->h;
                minNode->down->parent = minNode;  
                addNodeOpenList(minNode->down);
            }
            else{
                if (minNode->g + 1 < minNode->down->g){
                    minNode->down->g = 1 + minNode->g;
                    minNode->down->f = minNode->down->g + minNode->down->h;
                    minNode->down->parent = minNode;  
                    addNodeOpenList(minNode->down);
                }
            } 
        }

        
        minNode = getMinNode (openListHead); 
        deleteNodeOpenList(minNode); 
        addNodeCloseList(minNode); 

        if (minNode == finalNode)break; 

    } 
    showRoute(finalNode); 
    printf("\n");
}


void constructor(){
    openListHead = (struct nodeList *)malloc(sizeof(struct nodeList));
    closeListHead = (struct nodeList *)malloc(sizeof(struct nodeList));
    openListHead->node=NULL; 
    closeListHead->node=NULL; 
    openListHead->next=NULL; 
    closeListHead->next=NULL; 

    head=(struct node *)malloc(sizeof(struct node));
    head->id=0;
    head->down=NULL;
    head->right=NULL; 
    head->left=NULL; 
    head->up=NULL; 

    IdHead = (struct nodeId *)malloc(sizeof(struct nodeId));
    IdHead->id=-1; 
    IdHead->next=NULL; 
    
}

int main() {
   
    int raw = 8;
    int colum = 8;
    constructor(); 
    madeMatrix(raw, colum);
    blockNodes(); 
    pathfinding(7, 55); 
    printMatrix(); 
    return 0;
}
