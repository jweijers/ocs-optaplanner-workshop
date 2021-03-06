/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.training.election.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.Arrays;
import java.util.List;


@PlanningSolution
public class Election {

    public static final String NORMAL_CANDIDATE = "Normal candidate";
    // The candidate that games the system to win
    public static final String GAMER_CANDIDATE = "Gamer candidate";

    private HardSoftScore score;


    private String[] candidates = new String[]{NORMAL_CANDIDATE, GAMER_CANDIDATE};

    private List<FederalState> federalStateList;


    private Election() {
    }

    public Election(List<FederalState> federalStateList) {
        this.federalStateList = federalStateList;
    }

    @PlanningEntityCollectionProperty
    public List<FederalState> getFederalStateList() {
        return federalStateList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(final HardSoftScore score) {
        this.score = score;
    }

    @ValueRangeProvider(id = "candidates")
    @ProblemFactCollectionProperty
    public List<String> getCandidates() {
        return Arrays.asList(candidates);
    }
}
