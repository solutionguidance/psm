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

package gov.medicaid.binders;

import gov.medicaid.domain.model.AttachedDocumentsType;
import gov.medicaid.domain.model.CountyContractType;
import gov.medicaid.domain.model.DocumentType;
import gov.medicaid.domain.model.ApplicationType;
import gov.medicaid.domain.model.FacilityCredentialsType;
import gov.medicaid.domain.model.ProviderInformationType;
import gov.medicaid.domain.model.StatusMessageType;
import gov.medicaid.domain.model.StatusMessagesType;
import gov.medicaid.entities.CMSUser;
import gov.medicaid.entities.Application;
import gov.medicaid.entities.dto.FormError;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This binder handles the organization disclosure.
 */
public class TCMContractFormBinder extends BaseFormBinder {

    private static final String CONTRACT_WITH_COUNTY = "Contract With County";
    /**
     * The namespace for this form.
     */
    public static final String NAMESPACE = "_28_";

    /**
     * Creates a new binder.
     */
    public TCMContractFormBinder() {
        super(NAMESPACE);
    }

    /**
     * Binds the request to the model.
     * @param application the model to bind to
     * @param request the request containing the form fields
     *
     * @return
     * @throws BinderException if the format of the fields could not be bound properly
     */
    public List<BinderException> bindFromPage(CMSUser user, ApplicationType applicationType, HttpServletRequest request) {
        ProviderInformationType provider = XMLUtility.nsGetProvider(applicationType);
        FacilityCredentialsType credentials = XMLUtility.nsGetFacilityCredentials(applicationType);
        CountyContractType countyInfo = new CountyContractType();
        credentials.setContractWithCounty(countyInfo);

        String attachmentId = (String) request.getAttribute(NAMESPACE + "contractAttachment");
        if (attachmentId != null) {
            replaceDocument(XMLUtility.nsGetAttachments(provider), attachmentId, CONTRACT_WITH_COUNTY);
        }

        AttachedDocumentsType attachments = XMLUtility.nsGetAttachments(provider);
        attachmentId = (String) request.getAttribute(NAMESPACE + "coverSheet1");
        if (attachmentId != null) {
            countyInfo.setContractAttachmentObjectId(attachmentId);
            replaceDocument(attachments, attachmentId, "DHS-5638");
        }

        attachmentId = (String) request.getAttribute(NAMESPACE + "coverSheet2");
        if (attachmentId != null) {
            countyInfo.setContractAttachmentObjectId(attachmentId);
            replaceDocument(attachments, attachmentId, "DHS-5639");
        }

        attachmentId = (String) request.getAttribute(NAMESPACE + "coverSheet3");
        if (attachmentId != null) {
            countyInfo.setContractAttachmentObjectId(attachmentId);
            replaceDocument(attachments, attachmentId, "DHS-5702");
        }

        return Collections.emptyList();
    }

    /**
     * Binds the model to the request attributes.
     * @param application the model to bind from
     * @param mv the model and view to bind to
     * @param readOnly true if the view is read only
     */
    public void bindToPage(CMSUser user, ApplicationType applicationType, Map<String, Object> mv, boolean readOnly) {
        attr(mv, "bound", "Y");
        ProviderInformationType provider = XMLUtility.nsGetProvider(applicationType);
        AttachedDocumentsType attachments = XMLUtility.nsGetAttachments(provider);
        List<DocumentType> attachment = attachments.getAttachment();
        for (DocumentType doc : attachment) {
            if ("DHS-5638".equals(doc.getName())) {
                attr(mv, "coverSheet1", doc.getObjectId());
            } else if ("DHS-5639".equals(doc.getName())) {
                attr(mv, "coverSheet2", doc.getObjectId());
            } else if ("DHS-5702".equals(doc.getName())) {
                attr(mv, "coverSheet3", doc.getObjectId());
            } else if (CONTRACT_WITH_COUNTY.equals(doc.getName())) {
                attr(mv, "contractAttachment", doc.getObjectId());
            }
        }

    }

    private void replaceDocument(AttachedDocumentsType attachments, String id, String value) {
        List<DocumentType> toRemove = new ArrayList<DocumentType>();
        List<DocumentType> attachment = attachments.getAttachment();
        for (DocumentType doc : attachment) {
            if (id.equals(doc.getObjectId())) {
                doc.setName(value);
            } else if (value.equals(doc.getName())) {
                toRemove.add(doc);
            }
        }

        attachments.getAttachment().removeAll(toRemove);
    }

    /**
     * Captures the error messages related to the form.
     * @param application the application that was validated
     * @param messages the messages to select from
     *
     * @return the list of errors related to the form
     */
    protected List<FormError> selectErrors(ApplicationType applicationType, StatusMessagesType messages) {
        List<FormError> errors = new ArrayList<FormError>();

        List<StatusMessageType> ruleErrors = messages.getStatusMessage();
        List<StatusMessageType> caughtMessages = new ArrayList<StatusMessageType>();

        synchronized (ruleErrors) {
            for (StatusMessageType ruleError : ruleErrors) {
                int count = errors.size();

                String path = ruleError.getRelatedElementPath();
                if (path == null) {
                    continue;
                }

                if (errors.size() > count) { // caught
                    caughtMessages.add(ruleError);
                }
            }

            // so it does not get processed anywhere again
            ruleErrors.removeAll(caughtMessages);
        }

        return errors.isEmpty() ? NO_ERRORS : errors;
    }

    /**
     * Binds the fields of the form to the persistence model.
     *
     * @param applicationType the front end model
     * @param application the persistent model
     */
    public void bindToHibernate(ApplicationType applicationType, Application application) {
    }

    /**
     * Binds the fields of the persistence model to the front end xml.
     *
     * @param application the persistent model
     * @param applicationType the front end model
     */
    public void bindFromHibernate(Application application, ApplicationType applicationType) {
    }
}
