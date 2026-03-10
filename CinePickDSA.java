import java.util.Scanner;

/* ===============================
   MOVIE DATA MODEL
   =============================== */
class Movie {

    String title;
    String genre;
    double imdb;
    double collection;
    String cast;

    Movie(String title,String genre,double imdb,double collection,String cast){
        this.title=title;
        this.genre=genre;
        this.imdb=imdb;
        this.collection=collection;
        this.cast=cast;
    }

    public String toString(){
        return String.format("%-25s | IMDB: %.1f | BoxOffice: %.0f | Genre: %s",
                title, imdb, collection, genre);
    }
}

/* ===============================
   NODE CLASS
   =============================== */
class MovieNode{

    Movie movie;
    MovieNode next;

    MovieNode(Movie movie){
        this.movie=movie;
        this.next=null;
    }
}

/* ===============================
   CO2 : LINKED LIST
   =============================== */
class MovieLinkedList{

    MovieNode head;
    int size=0;

    void insert(Movie m){

        MovieNode node=new MovieNode(m);

        if(head==null){
            head=node;
        }
        else{

            MovieNode temp=head;

            while(temp.next!=null)
                temp=temp.next;

            temp.next=node;
        }

        size++;
    }

    void display(){

        MovieNode temp=head;

        while(temp!=null){
            System.out.println(temp.movie);
            temp=temp.next;
        }
    }

    Movie[] toArray(){

        Movie arr[]=new Movie[size];

        MovieNode temp=head;
        int i=0;

        while(temp!=null){
            arr[i++]=temp.movie;
            temp=temp.next;
        }

        return arr;
    }

    void rebuild(Movie arr[]){

        head=null;
        size=0;

        for(Movie m:arr)
            insert(m);
    }
}

/* ===============================
   CO3 : STACK
   =============================== */
class HistoryStack{

    MovieNode top;

    void push(Movie m){

        MovieNode node=new MovieNode(m);

        node.next=top;
        top=node;
    }

    void display(){

        if(top==null){
            System.out.println("History empty");
            return;
        }

        MovieNode temp=top;

        while(temp!=null){
            System.out.println(temp.movie.title);
            temp=temp.next;
        }
    }
}

/* ===============================
   CO3 : QUEUE
   =============================== */
class WatchQueue{

    MovieNode front,rear;

    void enqueue(Movie m){

        MovieNode node=new MovieNode(m);

        if(rear==null){
            front=rear=node;
            return;
        }

        rear.next=node;
        rear=node;
    }

    Movie dequeue(){

        if(front==null)
            return null;

        MovieNode temp=front;

        front=front.next;

        if(front==null)
            rear=null;

        return temp.movie;
    }
}

/* ===============================
   CO3 : CIRCULAR QUEUE
   =============================== */
class CircularRecommendQueue{

    Movie arr[];
    int front=-1,rear=-1,size;

    CircularRecommendQueue(int n){

        size=n;
        arr=new Movie[n];
    }

    void enqueue(Movie m){

        if((rear+1)%size==front){
            System.out.println("Circular Queue Full");
            return;
        }

        if(front==-1)
            front=0;

        rear=(rear+1)%size;

        arr[rear]=m;

        System.out.println("Recommended: "+m.title);
    }
}

/* ===============================
   CO4 : HASH TABLE
   =============================== */
class MovieHashTable{

    MovieNode table[];
    int capacity;

    MovieHashTable(int cap){

        capacity=cap;
        table=new MovieNode[cap];
    }

    int hash(String key){

        int h=0;

        for(int i=0;i<key.length();i++)
            h=37*h+key.charAt(i);

        h%=capacity;

        if(h<0) h+=capacity;

        return h;
    }

    void insert(Movie m){

        int index=hash(m.title.toLowerCase());

        MovieNode node=new MovieNode(m);

        node.next=table[index];
        table[index]=node;
    }

    Movie search(String title){

        int index=hash(title.toLowerCase());

        MovieNode temp=table[index];

        while(temp!=null){

            if(temp.movie.title.equalsIgnoreCase(title))
                return temp.movie;

            temp=temp.next;
        }

        return null;
    }
}

/* ===============================
   CO4 : PRIORITY QUEUE (MAX HEAP)
   =============================== */
class MovieMaxHeap{

    Movie heap[];
    int size=0;

    MovieMaxHeap(int cap){
        heap=new Movie[cap];
    }

    void insert(Movie m){

        if(size==heap.length)
            return;

        heap[size]=m;

        int current=size;
        size++;

        while(current>0 && heap[current].imdb > heap[(current-1)/2].imdb){

            Movie temp=heap[current];
            heap[current]=heap[(current-1)/2];
            heap[(current-1)/2]=temp;

            current=(current-1)/2;
        }
    }

    Movie peek(){

        if(size==0)
            return null;

        return heap[0];
    }
}

/* ===============================
   MAIN APPLICATION
   =============================== */
public class CinePickDSA{

    static MovieLinkedList db=new MovieLinkedList();
    static HistoryStack history=new HistoryStack();
    static WatchQueue queue=new WatchQueue();
    static CircularRecommendQueue circular=new CircularRecommendQueue(5);
    static MovieMaxHeap heap=new MovieMaxHeap(30);
    static MovieHashTable hash=new MovieHashTable(20);

    static Scanner sc=new Scanner(System.in);

    public static void main(String args[]){

        loadMovies();

        int ch;

        do{

            System.out.println("\n==== 🎬 CINEPICK MOVIE RECOMMENDATION SYSTEM ====");
            System.out.println("1 Display Movies (Linked List)");
            System.out.println("2 Search Movie (Linear Search)");
            System.out.println("3 Top Rated Movies (Bubble Sort)");
            System.out.println("4 Box Office Ranking (Merge Sort)");
            System.out.println("5 Add to Watch Later (Queue Enqueue)");
            System.out.println("6 Play Next Movie (Queue Dequeue)");
            System.out.println("7 Recently Watched (Stack)");
            System.out.println("8 Daily Recommendations (Circular Queue)");
            System.out.println("9 Editor’s Choice (Priority Queue / Heap)");
            System.out.println("10 Instant Movie Lookup (Hash Table)");
            System.out.println("0 Exit");
            System.out.print("Enter choice: ");

            ch=sc.nextInt();
            sc.nextLine();

            switch(ch){

                case 1:
                    db.display();
                    break;

                case 2:
                    System.out.print("Enter movie title: ");
                    linearSearch(sc.nextLine());
                    break;

                case 3:
                    bubbleSort();
                    break;

                case 4:
                    mergeSortMovies();
                    break;

                case 5:
                    System.out.print("Enter movie title: ");
                    Movie m1 = hash.search(sc.nextLine());

                    if(m1!=null){
                        queue.enqueue(m1);
                        System.out.println("Added to Watch Later!");
                    }
                    else{
                        System.out.println("Movie not found.");
                    }
                    break;

                case 6:

                    Movie next = queue.dequeue();

                    if(next!=null){
                        System.out.println("Now watching: "+next.title);
                        history.push(next);
                    }
                    else{
                        System.out.println("Watch Later queue empty.");
                    }
                    break;

                case 7:
                    history.display();
                    break;

                case 8:
                    Movie top = heap.peek();

                    if(top!=null)
                        circular.enqueue(top);

                    break;

                case 9:
                    Movie best = heap.peek();

                    if(best!=null)
                        System.out.println("Highest Rated Movie: "+best);

                    break;

                case 10:

                    System.out.print("Enter movie title: ");

                    Movie fastSearch = hash.search(sc.nextLine());

                    if(fastSearch!=null)
                        System.out.println("Found: "+fastSearch);
                    else
                        System.out.println("Movie not found.");

                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        }while(ch!=0);

    }

    static void loadMovies(){

        Movie initialData[] = {

            new Movie("RRR","Action",7.8,1387,"Ram Charan"),
            new Movie("Interstellar","Sci-Fi",8.7,773,"Matthew McConaughey"),
            new Movie("Inception","Sci-Fi",8.8,836,"Leonardo DiCaprio"),
            new Movie("Avengers Endgame","Action",8.4,2799,"Robert Downey Jr."),
            new Movie("John Wick 4","Action",7.7,440,"Keanu Reeves")
        };

        for(Movie m : initialData){

            db.insert(m);
            heap.insert(m);
            hash.insert(m);
        }
    }

    static void linearSearch(String title){

        MovieNode temp=db.head;

        while(temp!=null){

            if(temp.movie.title.equalsIgnoreCase(title)){

                System.out.println(temp.movie);
                return;
            }

            temp=temp.next;
        }

        System.out.println("Movie not found.");
    }

    static void bubbleSort(){

        Movie arr[]=db.toArray();

        for(int i=0;i<arr.length-1;i++)
            for(int j=0;j<arr.length-i-1;j++)
                if(arr[j].imdb<arr[j+1].imdb){

                    Movie t=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=t;
                }

        db.rebuild(arr);
        db.display();
    }

    static void mergeSortMovies(){

        Movie arr[]=db.toArray();

        mergeSort(arr,0,arr.length-1);

        db.rebuild(arr);
        db.display();
    }

    static void mergeSort(Movie arr[],int l,int r){

        if(l<r){

            int m=(l+r)/2;

            mergeSort(arr,l,m);
            mergeSort(arr,m+1,r);

            merge(arr,l,m,r);
        }
    }

    static void merge(Movie arr[],int l,int m,int r){

        int n1=m-l+1;
        int n2=r-m;

        Movie L[]=new Movie[n1];
        Movie R[]=new Movie[n2];

        for(int i=0;i<n1;i++) L[i]=arr[l+i];
        for(int j=0;j<n2;j++) R[j]=arr[m+1+j];

        int i=0,j=0,k=l;

        while(i<n1 && j<n2){

            if(L[i].collection>R[j].collection)
                arr[k++]=L[i++];
            else
                arr[k++]=R[j++];
        }

        while(i<n1) arr[k++]=L[i++];
        while(j<n2) arr[k++]=R[j++];
    }
}