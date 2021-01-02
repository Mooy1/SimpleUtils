package io.github.mooy1.slimegrid.implementation.grid;

import lombok.Getter;
import lombok.Setter;

public final class GridGenerator {

    @Setter
    @Getter
    private int generation;
    private final int hashcode;

    public GridGenerator(int hashcode) {
        this.hashcode = hashcode;
        this.generation = 0;
    }
    
    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GridGenerator && obj.hashCode() == this.hashcode;
    }

}
