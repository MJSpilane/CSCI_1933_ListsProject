import java.net.NoRouteToHostException;

public class ArrayList <T extends Comparable<T>> implements List<T>{
    T[] list;//generic list object
    int front;//automatically set to 0
    private boolean isSorted;
    int endValue=-1;//index of the last element in the array
    public ArrayList(){
        list = (T[]) new Comparable[5];//sets to default length of 5
        this.isSorted = true;
    }
    public ArrayList(int size){
        list = (T[]) new Comparable[size];
        this.isSorted = true;
    }
    @Override
    public boolean add(T element) {
        if (element == null){
            return false;
        }
        if(endValue==list.length-1){ //the array is full, must resize
            try {
                T[] biggerArray = (T[]) new Comparable[list.length * 2 + 1];
                System.arraycopy(list, 0, biggerArray, 0, list.length);
                list = biggerArray;
            }catch(OutOfMemoryError e){
                throw new OutOfMemoryError("current list length is:" + list.length);
            }
        }
        if(endValue ==-1){//no elements have been added to the list yet
            list[0] = element;
            endValue =0;
            return true;
        }
        list[endValue+1] = element;
        endValue+=1;
        if(size() > 1){
            if (isSorted) {
                if (element.compareTo(list[endValue -1]) < 0) {
                    isSorted = false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean add(int index, T element) {
        if (index < 0 || index > size()-1){//index is out of bounds
            return false;
        }
        if(list[index] == null){ //nothing in the way
            list[index] = element;
            return true;
        }
        if(endValue==list.length){
            try{
                T[] biggerArray = (T[]) new Comparable[list.length*2+1];
                System.arraycopy(list,0,biggerArray,0,list.length);
                list = biggerArray;
            }catch(OutOfMemoryError e){
                throw new OutOfMemoryError("current list length is" + list.length);
            }
        }
        T moving = list[index];
        T next;
        for(int i = index; i<list.length;i++){
            next = list[i];
            list[i] = moving;
            moving = next;
        }
        list[index] = element;
        endValue+=1;
        return true;
    }

    @Override
    public void clear() {
        for(int i =0; i<list.length; i++){
            list[i] = null;
        }
        this.isSorted = true;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index> size()-1){
            return null;
        }
        return list[index];
    }

    @Override
    public int indexOf(T element) {
        if(element == null){
            return -1;
        }
        for(int i =0; i<size(); i++){
            if(list[i].equals(element)){
                return i;
            }
            if(isSorted){
                if(list[i].compareTo(element) > 0){ //we are past where the element would be if it was in the list
                    return -1;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        for(int i =0;i<list.length;i++){
            if (list[i] != null){
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {

        int size=0;
        for(int i =0; i<list.length;i++){
            if(list[i] != null){
                size+=1;
            }
        }
        return size;
    }

    @Override
    public void sort() { //utilizing a bubble sort technique
        int i, j;
        T temp;
        boolean swapped = true;
        if(isSorted){
            return;
        }
        if(size() == 1 || size() == 0){
            isSorted = true;
            return;
        }
        for(i=0;i<size() && swapped == true; i++){
            swapped =false;
            for(j=1;j<size()-i;j++){
                if(list[j].compareTo(list[j-1]) < 0){
                    swapped = true;
                    temp = list[j];
                    list[j] = list[j-1];
                    list[j-1] = temp;
                }
            }
        }
        isSorted = true;
    }

    @Override
    public T remove(int index) {
        if(index < 0 || index>endValue) { //index is out of bounds
            return null;
        }
        if(isEmpty()){
            return null;
        }
        T value = list[index];
        list[index] = null;
        if(index == endValue){ //removing the last element in the list
            endValue-=1;
            return value;
        }
        for(int i =index; i<endValue;i++){
            list[i] = list[i+1];
        }
        list[endValue] = null;
        endValue-=1;
        isSorted = true;
        if(endValue ==0){
            return value;
        }
        for (int i = 1; i < size()-1; i++) {
           if(list[i].compareTo(list[i-1]) < 0){
               isSorted = false;
           }
        }
        return value;
    }

    @Override
    public void equalTo(T element) {
        if(element == null){
            return;
        }
        for(int i =0; i<endValue; i++){
            if (list[i].equals(element)) { //all elements that are not equal to the element in the parameter are reset to null
                continue;
            }
            else{
                remove(i);
                i-=1;
            }
        }
        if(!(list[endValue].equals(element))){
            remove(endValue);
        }
        isSorted = true;
    }

    @Override
    public void reverse() {
        if(size() <=1){
            return;
        }
        T temp;
        for(int i=0; i<list.length/2; i++){
            temp = list[i];
            list[i] = list[list.length-1-i];
            list[list.length-1-i]= temp;
        }
        int numOfNulls = list.length-size();
        //shift everything by list.length-size() to the left, the first list.length-size() items move to the last spots
        for(int i =numOfNulls; i<list.length; i++){
            list[i-numOfNulls] = list[i];
        }
        for(int i=size()+1;i<list.length;i++){
            list[i]=null;
        }
        isSorted = false;
    }

    @Override
    public void merge(List<T> otherList) {
        if(otherList.isEmpty()){
            return;
        }
        ArrayList<T> other = (ArrayList<T>) otherList;
        sort();
        other.sort();
        for(int i =0; i < other.size()-1; i++){ //walk through other list
            if(get(endValue)!= null) {
                if (other.get(i).compareTo(get(endValue)) > 0) {
                    add(other.get(i));
                    continue;
                }
            }
            for(int j =0; j<size()-1; j++){ //walk through this list
                if(other.get(i).compareTo(get(j)) <=0){
                    add(j,other.get(i));
                    break;
                }
            }
        }
    }

    @Override
    public void pairSwap() {
        T temp;
        if(size()%2 == 0){ //length is even
            for(int i =0; i<size(); i+=2){
                temp = list[i];
                list[i] = list[i+1];
                list[i+1]=temp;
            }
        }
        else{ //length is odd
            for(int i =0; i<size()-1; i+=2){
                temp = list[i];
                list[i]= list[i+1];
                list[i+1] = temp;
            }
        }
    }

    @Override
    public boolean isSorted() {
        return isSorted;
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.size());
        List<Integer> Blist = new ArrayList<Integer>();
        Blist.add(1);
        Blist.add(35);
        Blist.add(33);
        Blist.add(3);
        list.merge(Blist);
        for(int i =0; i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}
