/*
 * Copyright 2012, 2013 TopCoder, Inc.
 * Copyright 2018 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.medicaid.entities;

/**
 * Represents supported templates.
 */
public enum EmailTemplate {

    /**
     * Password Templates
     */
    FORGOT_PASSWORD,
    UPDATE_PASSWORD,

    /**
     * Registration Templates
     */
    NEW_REGISTRATION,
    NEW_REGISTRATION_BY_ADMIN,

    /**
     * Application Templates
     */
    APPROVED_APPLICATION,
    MODIFIED_APPLICATION,
    PENDING_APPLICATION,
    REJECTED_APPLICATION
}
