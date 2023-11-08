#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "dataprocessing.c"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <WinSock2.h>
#include <time.h>
#pragma comment(lib, "ws2_32.lib")

int points=0; 
SOCKET serverSocket;

int ghostOneFP    = -1; 
int ghostTwoFP    = -1; 
int ghostThreeFP  = -1; 
int ghostFourFP = -1; 
int lifes = 3;
int lastPoints = 0;  

int followedTimes = 0;

int ended = -1; 

int matrix[15][15] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

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

char route[500] = "";

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
    if (openListHead == NULL || openListHead->node==NULL){
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
        //printf("%i ", current->id); 
        sprintf(route + strlen(route), "%i ", current->id);
        current = current->next;
    }
}
/*-----------------------------------------------------------------------*/

/*-------------------------Pathfinding Logic-----------------------------*/

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
            //printf("%i ", auxiliar->id);
            auxiliar=auxiliar->right; 
            
        }
        //printf("\n");
    }    
    head=NULL;

}

int convertX(int id){
    return id%15; 
}

int convertY(int id){
    return id/15; 
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
    for (int i=0; i<15; i++){
        for (int j=0; j<15; j++){
            if (matrix[i][j]==1)
                addNodeCloseList(getNodeById(i*15+j));
        }
    }
}

void pathfinding(int inicialNodeId, int finalNodeId){

    defineH(finalNodeId);
    struct node *minNode = getNodeById(inicialNodeId); 
    struct node *finalNode = getNodeById(finalNodeId);
    addNodeCloseList(minNode);  
    minNode->f = minNode->h; 
    minNode->parent = NULL; 
    
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
//-------------------------------------------------//
////////////////////////////////////////////////////
///////Server///////////////////////////////////////
////////////////////////////////////////////////////
/////////////////////Server/////////////////////////
////////////////////////////////////////////////////
/////////////////////////////////////Server/////////
////////////////////////////////////////////////////
//-----------------------Server-------------------//
void restart(){
   int matrix_[15][15] ={
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        for (int i = 0; i<15; i++){
            for(int j = 0; j<15; j++){
                matrix[i][j] = matrix_[i][j]; 
            }
        }
    points = 0; 
    ghostOneFP   = -1; 
    ghostTwoFP   = -1; 
    ghostThreeFP = -1; 
    ghostFourFP  = -1; 
    lifes = 3;
    lastPoints = 0; 
}

int randNumber(int id){
    int x = rand()%225; 
    int posX = convertX(x);
    int posY = convertY(x);
    while (matrix[posY][posX]==1 || x==id){
        x = rand()%225;  
        posX = convertX(x);
        posY = convertY(x);
        
    }
    return x;  
}

int initServer(int serverPort) {
    WSADATA wsaData;

    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        fprintf(stderr, "Error al inicializar Winsock.\n");
        return 1;
    }

    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == INVALID_SOCKET) {
        fprintf(stderr, "Error al crear el socket del servidor.\n");
        WSACleanup();
        return 1;
    }

    struct sockaddr_in serverAddr;
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(serverPort);
    serverAddr.sin_addr.s_addr = INADDR_ANY;

    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        fprintf(stderr, "Error al enlazar el socket del servidor.\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }

    if (listen(serverSocket, 1) == SOCKET_ERROR) {
        fprintf(stderr, "Error al poner el servidor en modo escucha.\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }

    return 0;
}

void receiveMessage() {
    struct sockaddr_in clientAddr;
    int clientAddrSize = sizeof(clientAddr);
    SOCKET clientSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &clientAddrSize);

    if (clientSocket == INVALID_SOCKET) {
        fprintf(stderr, "Error al aceptar la conexión del cliente.\n");
        closesocket(serverSocket);
        WSACleanup();
        return;
    }

    char buffer[256];
    int bytesRead = recv(clientSocket, buffer, sizeof(buffer), 0);

    if (bytesRead > 0) {
        buffer[bytesRead] = '\0';
        //printf("Mensaje del cliente: %s\n", buffer);
        const char* response = "RECIBIDO";
        if (buffer[0]=='p'){
            ended = 0;
            int start = extractNumber(buffer, 2); 
            int end = extractNumber(buffer, 3);
            int raw = 15;
            int colum = 15;

            if(buffer[2]=='1')
                ghostOneFP   = end;  
            if(buffer[2]=='2'){

                int pacX = convertX(end); 
                int pacY = convertY(end); 
                int ghoX = convertX(start); 
                int ghoY = convertY(start); 

                int delta = 5;
                //printf("%i %i\n",abs(pacX - ghoX), abs(pacY - ghoY)); 
                if (abs(pacX - ghoX)>delta || abs(pacY - ghoY)>delta){
                    end = randNumber(start); 
                    //printf("Entró \n");
                }
                ghostTwoFP   = end; 
                
            }
            if(buffer[2]=='3'){ 
                end =randNumber(start); 
                ghostThreeFP = end;
            }
            if(buffer[2]=='4'){
                if(followedTimes%5!=0)
                    end = randNumber(start);
                ghostFourFP= end; 
                followedTimes++; 

            }
               
            
            
            printf("%i %i \n", start, end); 
            constructor(); 
            madeMatrix(raw, colum);
            blockNodes(); 
            pathfinding(start, end); 
            printMatrix(); 
            


            response = route; 

            ended = 1; 
        }
        else if (buffer[0] == 'R'){
            int pacY = extractNumber(buffer, 1); 
            int pacX = extractNumber(buffer, 2); 

            if (pacY+1==15 || matrix[pacX][pacY+1]==1)
                response = "false"; 
            else{
                response = "true"; 
            }
        }
        else if (buffer[0] == 'L'){
            int pacY = extractNumber(buffer, 1); 
            int pacX = extractNumber(buffer, 2); 
            if (pacY-1==-1 || matrix[pacX][pacY-1]==1)
                response = "false"; 
            else{
                response = "true"; 
            }
        }
        else if (buffer[0] == 'U'){
            int pacY = extractNumber(buffer, 1); 
            int pacX = extractNumber(buffer, 2); 
            if (pacX-1==-1 || matrix[pacX-1][pacY]==1)
                response = "false"; 
            else{
                response = "true"; 
            }
        }
        else if (buffer[0] == 'D'){
            int pacY = extractNumber(buffer, 1); 
            int pacX = extractNumber(buffer, 2); 
            if (pacX+1==15 || matrix[pacX+1][pacY]==1)
                response = "false"; 
            else{
                response = "true"; 
            }
        } 
        
        else if (buffer[0] == 'F') {
            //printf("%s\n", "Nueva fruta!!");
            int pacY = extractNumber(buffer, 1);
            int pacX = extractNumber(buffer, 2);
            if (matrix[pacY][pacX] == 1)
                response = "false"; // Copia la cadena "false" en response
            else {
                matrix[pacY][pacX] = 3;
                response = "true"; // Copia la cadena "true" en response
            }
        }
        else if (buffer[0] == 'q') {
            //printf("%s\n", "Nueva fruta!!");
            int posPower = randNumber(-1);
            int pacX = convertX(posPower); 
            int pacY = convertY(posPower); 
            matrix[pacY][pacX] = 4;
            char cadena[20];
            sprintf(cadena, "%d", posPower);
            response = cadena;
            
        }
        
        

        
        else if (buffer[0] == 'S'){
            int pacY = extractNumber(buffer, 1); 
            int pacX = extractNumber(buffer, 2); 

            if (matrix[pacY][pacX] == 0 ){
                matrix[pacY][pacX] = -1; 
                points+=250;
                char cadena[20]; 
                sprintf(cadena, "%d", points);
                response = cadena; 
            }
            else if (matrix[pacY][pacX] == 3) {
                matrix[pacY][pacX] = -1;
                points += 1000;
                char cadena[20];
                sprintf(cadena, "%d", points);
                response = cadena;
            }
            else if (matrix[pacY][pacX] == 4) {
                matrix[pacY][pacX] = -1;
                points += 0;
                char cadena[20];
                response = "pacManAttack";
            }
            else
                response = "empty";
            
        }              
        else if (buffer[0] == 'N'){
            restart(); 
            response = "REINICIO"; 
        }
       
        else if (buffer[0] == 'g'){
            int posGO = extractNumber(buffer, 1); 
            if (posGO == ghostOneFP || ghostOneFP == -1)
                response = "true"; 
            
            else{
                response = "false"; 
            }
        
        }
        else if (buffer[0] == 'G'){
            int posGO = extractNumber(buffer, 1); 
           if (posGO == ghostTwoFP || ghostTwoFP == -1){
                response = "true"; 
            }
            else{
                response = "false"; 
            }
        
        }
        else if (buffer[0] == 'h'){
            int posGO = extractNumber(buffer, 1); 
            if (posGO == ghostThreeFP || ghostThreeFP == -1){
                response = "true"; 
                ended = 0;
            }
            else{
                response = "false"; 
            }
        
        }
        else if (buffer[0] == 'H'){
            int posGO = extractNumber(buffer, 1); 
            printf("%i", posGO);
            printf("%i \n", ghostFourFP);
            
            if (posGO == ghostFourFP || ghostFourFP == -1){
                response = "true"; 
                ended = 0;
            }
            else{
                response = "false"; 
            }
            
            
        }
        
        else if (buffer[0] == 'o'){
            int points = extractNumber(buffer, 1); 
            if (points-lastPoints>=10000){
                char cadena[20]; 
                lifes++; 
                lastPoints = points; 
            }
            char cadena[20];
            sprintf(cadena, "%d", lifes );
            response = cadena; 
        }
        
        else if (buffer[0] == 'c'){
            int pacID = extractNumber(buffer, 1);
            int g1ID = extractNumber(buffer, 2);
            int g2ID = extractNumber(buffer, 3);
            int g3ID = extractNumber(buffer, 4);
            int g4ID = extractNumber(buffer, 5);

            if(pacID == g1ID || pacID == g2ID || pacID == g3ID ||pacID == g4ID ){
                lifes--; 
                if (lifes <= 0){
                    response = "killed";
                }
                else{
                    response = "minusLife"; 
                }
            }
        }
        send(clientSocket, response, strlen(response), 0);
        strcpy(route, ""); 
    }

    closesocket(clientSocket);
}

int main() {
    if (initServer(12345) != 0) {
        return 1;
    }

    while (1) {
        receiveMessage();
    }

    closesocket(serverSocket);
    WSACleanup();

    return 0;
}

