/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.kernel;

import org.apache.openjpa.lib.rop.ResultObjectProvider;

/**
 * Variant of {@link ResultObjectProvider} that populates a
 * {@link OpenJPAStateManager} object in an application-defined manner.
 *  Implementations of this interface can be used to customize data loading.
 *
 * @author Patrick Linskey
 * @see AbstractPCResultObjectProvider
 */
public interface PCResultObjectProvider
    extends ResultObjectProvider {

    /**
     * Initializes the state manager.
     *
     * @see StoreManager#initialize
     */
    void initialize(OpenJPAStateManager sm, PCState state,
        FetchConfiguration fetch)
        throws Exception;
}
