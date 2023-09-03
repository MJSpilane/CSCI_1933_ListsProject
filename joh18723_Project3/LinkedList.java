public class LinkedList <T extends Comparable<T>> implements List<T>{ //headed linked list
    Node<T> begin; //create a starter node that is null
    int length; //variable to keep track of the length of the linked list
    boolean isSorted;
    Node<T> ptr; // a pointer variable to navigate the list
    Node<T> trailer; //another pointer that will always be one behind ptr
    public LinkedList(){
        this.begin = new Node<T>(null,null);
        this.ptr = begin.getNext(); //pointer starts by pointing at start
        this.isSorted = true;
        this.length = 0;
    }
    @Override
    public boolean add(T element) {
        if(element == null){
            return false;
        }
        Node<T> newNode = new Node<>(element,null);
        trailer = begin;
        for(int i=0;i<length;i++){
            trailer = trailer.getNext();
        }
        trailer.setNext(newNode);
        this.length +=1;
        if(isSorted && length >1) {
            if (element.compareTo(trailer.getData()) < 0 ) { //the element added to the end makes the list unsorted
                isSorted = false;
            }
        }
        return true;
    }

    @Override
    public boolean add(int index, T element) {
        if (element == null) {
            return false;
        }
        if (index >= length || index < 0) {
            return false;
        }
        ptr = begin.getNext();
        trailer = begin;
        for (int i = 0; i < index; i++) { //iterates until counter is at the desired index(trailer is looking at the node before the desired index and ptr is looking at the node at the desired index)
            ptr = ptr.getNext();
            trailer = trailer.getNext();
        }
        //the index is not at the end of the list, we have to shift all nodes at the index and further down one
        Node<T> newNode = new Node<>(element, ptr);
        trailer.setNext(newNode);
        length += 1;
        if (isSorted && trailer!= begin) {
                if (element.compareTo(trailer.getData()) < 0){
                    isSorted = false;
                }
                if(element.compareTo(ptr.getData()) > 0){
                    isSorted = false;
                }
        }
        if(isSorted && length > 1){
            if(element.compareTo(ptr.getData()) > 0){
                isSorted = false;
            }
        }
        return true;
    }

    @Override
    public void clear() {
        begin.setNext(null);//will make the list impossible to get to, which will make java trash collect it
        length =0;
        isSorted = true;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index>length-1){
            return null;
        }
        ptr = begin.getNext();
        for(int i=0; i<index;i++){ //by the end of this for loop ptr is at the index
            ptr = ptr.getNext();
        }
        return ptr.getData();
    }

    @Override
    public int indexOf(T element){
        ptr=begin.getNext();
        int currentIndex = 0;
        for(int i =0; i<length;i++){
            if(ptr.getData() == element){
                return currentIndex;
            }
            if (isSorted) {
                if (ptr.getData().compareTo(element) > 0) {
                    return -1;
                }
            }
            ptr = ptr.getNext();
            currentIndex +=1;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        if(begin.getNext() == null){
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public void sort() {//bubble sort technique
        Node<T> current = begin, index = null;
        int temp;
        if(begin == null){
            isSorted = true;
            return;
        }
        else{
            while(current.getData() != null){
                index = current.getNext();
                while(index.getData() != null){
                    if(current.getData().compareTo(index.getData())>0){
                        swap(current, index);
                    }
                    index = index.getNext();
                }
                current = current.getNext();
            }
        }
        isSorted = true;
    }
    public void swap(Node<T> nodeA, Node<T> nodeB){ //swaps two nodes(for bubble sort and pair swap)
        if(nodeA.getData() == nodeB.getData()){
            return;
        }
        T temp = nodeA.getData();
        nodeA.setData(nodeB.getData());
        nodeB.setData(temp);
    }

    @Override
    public T remove(int index) {
        if(isEmpty()){
            return null;
        }
        if(index < 0 || index > length-1){
            return null;
        }
        ptr = begin.getNext();
        trailer = begin;
        for(int i=0;i<index;i++){ //iterates until counter is at the desired index(trailer is looking at the node before the desired index and ptr is looking at the node at the desired index)
            ptr = ptr.getNext();
            trailer = trailer.getNext();
        }
        T removedValue = ptr.getData();
        trailer.setNext(ptr.getNext());
        length-=1;
        if(length > 1) {
            ptr = begin.getNext().getNext();
            trailer = begin.getNext();
            isSorted = true;
            for (int i = 1; i < length; i++) {
                if (trailer.getData().compareTo(ptr.getData()) > 0) {
                    isSorted = false;
                    break;
                }
                ptr = ptr.getNext();
                trailer = trailer.getNext();
            }
        }
        else{
            isSorted = true;
        }
        return removedValue;
    }

    @Override
    public void equalTo(T element) {
        Node<T> equalPtr = begin.getNext();
        int counter =0;
        while(counter < length-1){
            if (!equalPtr.getData().equals(element)) {
                equalPtr = equalPtr.getNext();
                remove(counter);
            } else {
                equalPtr = equalPtr.getNext();
                counter+=1;
            }
        }
        if(!equalPtr.getData().equals(element)){
            remove(counter);
        }
        isSorted = true;
    }

    @Override
    public void reverse() {
        if(begin.getNext() == null || begin.getNext().getNext()==null){
            return; //only one element, nothing to reverse
        }
        ptr = begin.getNext().getNext();
        trailer = begin.getNext();
        while(ptr != null){
            trailer.setNext(ptr.getNext());
            ptr.setNext(begin.getNext());
            begin.setNext(ptr);
            ptr = trailer.getNext();
        }
        if(isSorted){
            isSorted = false;
        }
        else{
            ptr = begin.getNext().getNext();
            trailer = begin.getNext();
            isSorted = true;
            for(int i =0; i<length; i++){
                if(trailer.getData().compareTo(ptr.getData()) >0){
                    isSorted = false;
                }
            }
        }
    }

    @Override
    public void merge(List<T> otherList) {
        if(otherList.isEmpty()){
            return;
        }
        LinkedList<T> other = (LinkedList<T>) otherList; //type cast otherList to type LinkedList
        sort();
        other.sort();
        ptr=begin.getNext();
        trailer = begin;
        Node<T> otherPtr = other.begin.getNext();
        boolean  added;
        for(int i =0; i<other.length;i++){//loop to walk through other list
            added = false;
            for(int j=0; j<length; j++){//loop to walk through this list
                if(otherPtr.getData().compareTo(ptr.getData()) <= 0){
                    trailer.setNext(otherPtr);
                    otherPtr.setNext(ptr);
                    added = true;
                    break;
                }
            }
            if(added == false){ //must be larger than every element in list, so add it to the end
                ptr.setNext(otherPtr);
            }
            otherPtr = otherPtr.getNext();
        }
    }

    @Override
    public void pairSwap() {
        if(length <2){
            return;
        }
        trailer = begin.getNext();
        if(length%2 == 0){ //length of list is even
            for(int i =0; i<length;i+=2){
                swap(trailer,trailer.getNext());
                trailer = trailer.getNext().getNext();
            }
        }
        else{ //length of list must be odd
            for(int i =0; i<length -1; i+=2){
                swap(trailer, trailer.getNext());
                trailer = trailer.getNext().getNext();
            }
        }
        if(length > 1) {
            ptr = begin.getNext().getNext();
            trailer = begin.getNext();
            isSorted = true;
            for (int i = 1; i < length; i++) {
                if (trailer.getData().compareTo(ptr.getData()) > 0) {
                    isSorted = false;
                    break;
                }
                ptr = ptr.getNext();
                trailer = trailer.getNext();
            }
        }
        else{
            isSorted = true;
        }
    }

    @Override
    public boolean isSorted() {
        return isSorted;
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(1);
        list.add(2);
        list.add(4);
        LinkedList<Integer> listB = new LinkedList<Integer>();
        listB.add(2);
        listB.add(3);
        listB.add(8);
        list.merge(listB);
        for(int i=0;i<list.length; i++){
            System.out.println(list.get(i));
        }
    }
}
