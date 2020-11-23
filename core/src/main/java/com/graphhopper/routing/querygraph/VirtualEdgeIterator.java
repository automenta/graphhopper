/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing.querygraph;

import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EnumEncodedValue;
import com.graphhopper.routing.ev.IntEncodedValue;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.IntsRef;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.FetchMode;
import com.graphhopper.util.PointList;

import java.util.List;

/**
 * @author Peter Karich
 */
class VirtualEdgeIterator implements EdgeIterator {
    private final EdgeFilter edgeFilter;
    private List<EdgeIteratorState> edges;
    private int current;
    private transient EdgeIteratorState currentEdge = null;

    VirtualEdgeIterator(EdgeFilter edgeFilter, List<EdgeIteratorState> edges) {
        this.edgeFilter = edgeFilter;
        reset(edges);
    }

    EdgeIterator reset(List<EdgeIteratorState> edges) {
        this.edges = edges;
        current = -1;
        this.currentEdge = null;
        return this;
    }

    @Override
    public boolean next() {
        final int s = edges.size();
        do {
            if (++current >= s) {
                currentEdge = null;
                return false;
            }
        } while (!edgeFilter.accept(currentEdge = edges.get(current)));
        return true;
    }

    @Override
    public EdgeIteratorState detach(boolean reverse) {
        if (reverse)
            throw new IllegalStateException("Not yet supported");
        return currentEdge;
    }

    @Override
    public int getEdge() {
        return currentEdge.getEdge();
    }

    @Override
    public int getEdgeKey() {
        return currentEdge.getEdgeKey();
    }

    @Override
    public int getBaseNode() {
        return currentEdge.getBaseNode();
    }

    @Override
    public int getAdjNode() {
        return currentEdge.getAdjNode();
    }

    @Override
    public PointList fetchWayGeometry(FetchMode mode) {
        return currentEdge.fetchWayGeometry(mode);
    }

    @Override
    public EdgeIteratorState setWayGeometry(PointList list) {
        return currentEdge.setWayGeometry(list);
    }

    @Override
    public double getDistance() {
        return currentEdge.getDistance();
    }

    @Override
    public EdgeIteratorState setDistance(double dist) {
        return currentEdge.setDistance(dist);
    }

    @Override
    public IntsRef getFlags() {
        return currentEdge.getFlags();
    }

    @Override
    public EdgeIteratorState setFlags(IntsRef flags) {
        return currentEdge.setFlags(flags);
    }

    @Override
    public EdgeIteratorState set(BooleanEncodedValue property, boolean value) {
        currentEdge.set(property, value);
        return this;
    }

    @Override
    public boolean get(BooleanEncodedValue property) {
        return currentEdge.get(property);
    }

    @Override
    public EdgeIteratorState setReverse(BooleanEncodedValue property, boolean value) {
        currentEdge.setReverse(property, value);
        return this;
    }

    @Override
    public boolean getReverse(BooleanEncodedValue property) {
        return currentEdge.getReverse(property);
    }

    @Override
    public EdgeIteratorState set(IntEncodedValue property, int value) {
        currentEdge.set(property, value);
        return this;
    }

    @Override
    public int get(IntEncodedValue property) {
        return currentEdge.get(property);
    }

    @Override
    public EdgeIteratorState setReverse(IntEncodedValue property, int value) {
        currentEdge.setReverse(property, value);
        return this;
    }

    @Override
    public int getReverse(IntEncodedValue property) {
        return currentEdge.getReverse(property);
    }

    @Override
    public EdgeIteratorState set(DecimalEncodedValue property, double value) {
        currentEdge.set(property, value);
        return this;
    }

    @Override
    public double get(DecimalEncodedValue property) {
        return currentEdge.get(property);
    }

    @Override
    public EdgeIteratorState setReverse(DecimalEncodedValue property, double value) {
        currentEdge.setReverse(property, value);
        return this;
    }

    @Override
    public double getReverse(DecimalEncodedValue property) {
        return currentEdge.getReverse(property);
    }

    @Override
    public <T extends Enum> EdgeIteratorState set(EnumEncodedValue<T> property, T value) {
        currentEdge.set(property, value);
        return this;
    }

    @Override
    public <T extends Enum> T get(EnumEncodedValue<T> property) {
        return currentEdge.get(property);
    }

    @Override
    public <T extends Enum> EdgeIteratorState setReverse(EnumEncodedValue<T> property, T value) {
        currentEdge.setReverse(property, value);
        return this;
    }

    @Override
    public <T extends Enum> T getReverse(EnumEncodedValue<T> property) {
        return currentEdge.getReverse(property);
    }

    @Override
    public String getName() {
        return currentEdge.getName();
    }

    @Override
    public EdgeIteratorState setName(String name) {
        return currentEdge.setName(name);
    }

    @Override
    public String toString() {
        if (current >= 0 && current < edges.size()) {
            return "virtual edge: " + currentEdge + ", all: " + edges.toString();
        } else {
            return "virtual edge: (invalid)" + ", all: " + edges.toString();
        }
    }

    @Override
    public EdgeIteratorState copyPropertiesFrom(EdgeIteratorState edge) {
        return currentEdge.copyPropertiesFrom(edge);
    }

    @Override
    public int getOrigEdgeFirst() {
        return currentEdge.getOrigEdgeFirst();
    }

    @Override
    public int getOrigEdgeLast() {
        return currentEdge.getOrigEdgeLast();
    }

    public List<EdgeIteratorState> getEdges() {
        return edges;
    }
}
