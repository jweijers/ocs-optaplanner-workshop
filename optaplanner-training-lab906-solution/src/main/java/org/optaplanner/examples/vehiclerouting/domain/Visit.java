/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.vehiclerouting.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.solver.DepotAngleCustomerDifficultyWeightFactory;
import org.optaplanner.examples.vehiclerouting.domain.solver.VisitIndexUpdatingVariableListener;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedVisit;

@PlanningEntity(difficultyWeightFactoryClass = DepotAngleCustomerDifficultyWeightFactory.class)
@XStreamAlias("VrpVisit")
@XStreamInclude({
        TimeWindowedVisit.class
})
public class Visit extends AbstractPersistable implements Standstill {

    protected VisitType visitType;
    protected Location location;
    protected Ride ride;

    // Planning variables: changes during planning, between score calculations.
    protected Standstill previousStandstill;

    // Shadow variables
    protected Visit nextVisit;
    protected Vehicle vehicle;
    protected Integer visitIndex;

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRideSize() {
        return ride.getSize();
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @PlanningVariable(valueRangeProviderRefs = {"vehicleRange", "customerRange"},
            graphType = PlanningVariableGraphType.CHAINED)
    public Standstill getPreviousStandstill() {
        return previousStandstill;
    }

    public void setPreviousStandstill(Standstill previousStandstill) {
        this.previousStandstill = previousStandstill;
    }

    @Override
    public Visit getNextVisit() {
        return nextVisit;
    }

    @Override
    public void setNextVisit(Visit nextVisit) {
        this.nextVisit = nextVisit;
    }

    @Override
    @AnchorShadowVariable(sourceVariableName = "previousStandstill")
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @CustomShadowVariable(variableListenerClass = VisitIndexUpdatingVariableListener.class,
            // Arguable, to adhere to API specs (although this works), nextCustomer should also be a source,
            // because this shadow must be triggered after nextCustomer (but there is no need to be triggered by nextCustomer)
            sources = {@PlanningVariableReference(variableName = "previousStandstill")})
    public Integer getVisitIndex() {
        return visitIndex;
    }

    public void setVisitIndex(Integer visitIndex) {
        this.visitIndex = visitIndex;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    /**
     * @return a positive number, the distance multiplied by 1000 to avoid floating point arithmetic rounding errors
     */
    public long getDistanceFromPreviousStandstill() {
        if (previousStandstill == null) {
            throw new IllegalStateException("This method must not be called when the previousStandstill ("
                    + previousStandstill + ") is not initialized yet.");
        }
        return getDistanceFrom(previousStandstill);
    }

    /**
     * @param standstill never null
     * @return a positive number, the distance multiplied by 1000 to avoid floating point arithmetic rounding errors
     */
    public long getDistanceFrom(Standstill standstill) {
        return standstill.getLocation().getDistanceTo(location);
    }

    /**
     * @param standstill never null
     * @return a positive number, the distance multiplied by 1000 to avoid floating point arithmetic rounding errors
     */
    public long getDistanceTo(Standstill standstill) {
        return location.getDistanceTo(standstill.getLocation());
    }

    @Override
    public String toString() {
        if (location.getName() == null) {
            return super.toString();
        }
        return location.getName();
    }

}
