/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.heuristic.selector.move.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;
import org.optaplanner.core.config.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.entity.EntitySelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.value.ValueSelectorConfig;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.heuristic.selector.AbstractSelectorFactoryTest;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelectorFactory;
import org.optaplanner.core.impl.heuristic.selector.move.composite.UnionMoveSelector;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.multientity.TestdataHerdEntity;
import org.optaplanner.core.impl.testdata.domain.multientity.TestdataMultiEntitySolution;
import org.optaplanner.core.impl.testdata.domain.multivar.TestdataMultiVarSolution;

class ChangeMoveSelectorFactoryTest extends AbstractSelectorFactoryTest {

    @Test
    void deducibleMultiVar() {
        SolutionDescriptor solutionDescriptor = TestdataMultiVarSolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        moveSelectorConfig.setValueSelectorConfig(new ValueSelectorConfig("secondaryValue"));
        MoveSelector moveSelector = MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor), SelectionCacheType.JUST_IN_TIME, SelectionOrder.RANDOM);
        assertThat(moveSelector)
                .isInstanceOf(ChangeMoveSelector.class);
    }

    @Test
    void undeducibleMultiVar() {
        SolutionDescriptor solutionDescriptor = TestdataMultiVarSolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        moveSelectorConfig.setValueSelectorConfig(new ValueSelectorConfig("nonExistingValue"));
        assertThatIllegalArgumentException().isThrownBy(() -> MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor),
                SelectionCacheType.JUST_IN_TIME,
                SelectionOrder.RANDOM));
    }

    @Test
    void unfoldedMultiVar() {
        SolutionDescriptor solutionDescriptor = TestdataMultiVarSolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        MoveSelector moveSelector = MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor), SelectionCacheType.JUST_IN_TIME, SelectionOrder.RANDOM);
        assertThat(moveSelector)
                .isInstanceOf(UnionMoveSelector.class);
        assertThat(((UnionMoveSelector) moveSelector).getChildMoveSelectorList().size()).isEqualTo(3);
    }

    @Test
    void deducibleMultiEntity() {
        SolutionDescriptor solutionDescriptor = TestdataMultiEntitySolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        moveSelectorConfig.setEntitySelectorConfig(new EntitySelectorConfig(TestdataHerdEntity.class));
        MoveSelector moveSelector = MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor), SelectionCacheType.JUST_IN_TIME, SelectionOrder.RANDOM);
        assertThat(moveSelector)
                .isInstanceOf(ChangeMoveSelector.class);
    }

    @Test
    void undeducibleMultiEntity() {
        SolutionDescriptor solutionDescriptor = TestdataMultiEntitySolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        moveSelectorConfig.setEntitySelectorConfig(new EntitySelectorConfig(TestdataEntity.class));
        assertThatIllegalArgumentException().isThrownBy(() -> MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor),
                SelectionCacheType.JUST_IN_TIME,
                SelectionOrder.RANDOM));
    }

    @Test
    void unfoldedMultiEntity() {
        SolutionDescriptor solutionDescriptor = TestdataMultiEntitySolution.buildSolutionDescriptor();
        ChangeMoveSelectorConfig moveSelectorConfig = new ChangeMoveSelectorConfig();
        MoveSelector moveSelector = MoveSelectorFactory.create(moveSelectorConfig).buildMoveSelector(
                buildHeuristicConfigPolicy(solutionDescriptor), SelectionCacheType.JUST_IN_TIME, SelectionOrder.RANDOM);
        assertThat(moveSelector)
                .isInstanceOf(UnionMoveSelector.class);
        assertThat(((UnionMoveSelector) moveSelector).getChildMoveSelectorList().size()).isEqualTo(2);
    }

}
