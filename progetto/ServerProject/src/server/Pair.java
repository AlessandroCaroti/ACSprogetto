package server;

public class Pair {
    final private String value;
    final private int index;

    Pair(int idx, String val){
        this.index = idx;
        this.value = val;
    }

    public boolean equals(Pair p){
        return value.equals(p.value);
    }

    public boolean equals(String value){
        return value.equals(value);
    }

    public int getIndex(){
        return index;
    }

    public long hashCodee(){
        long hash = 5381;
        for(char c : value.toCharArray()){
            hash = ((hash << 5) + hash) + c;
        }
        return hash;
    }
}
