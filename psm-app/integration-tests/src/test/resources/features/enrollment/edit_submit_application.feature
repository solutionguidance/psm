Feature: Editing and Submitting Applications
  Users wish to edit and submit applications

  @issue_1105
  Scenario: Save as draft twice for Speech Language Pathologist application
    Given I have started an enrollment
    And I am on the individual summary page
    And I save the application as a draft
    And I save the application as a draft
    Then I should have no errors

  @issue_1104
  @ignore
  Scenario: Save as draft twice for Head Start application
    Given I have started an enrollment
    And I am on the organization summary page
    And I save the application as a draft
    And I save the application as a draft
    Then I should have no errors

  @issue_1105
  Scenario: Save and then submit Speech Language Pathologist application
    Given I have started an enrollment
    And I am on the individual provider statement page
    And I save the application as a draft
    When I enter my individual provider statement
    And I submit the enrollment
    Then I should have no errors

  @issue_1105
  Scenario: Save and then submit Head Start application
    Given I have started an enrollment
    And I am on the organization provider statement page
    And I save the application as a draft
    When I enter my organization provider statement
    And I submit the enrollment
    Then I should have no errors

  # Approve the application for the next test.
  Scenario: Admin approve Speech Language Pathologist application
    Given I am logged in as an admin
    And I am on the admin Pending page
    When I am on the Review Enrollment Page for '0000000006'
    And I Approve the Enrollment
    And I view the Reviewed Enrollment
    Then I should have no errors
    And The Enrollment should be in the 'Approved' state

  # Approve the application for the next test.
  Scenario: Admin approve Head Start application
    Given I am logged in as an admin
    And I am on the admin Pending page
    When I am on the Review Enrollment Page for '1234567893'
    And I Approve the Enrollment
    And I view the Reviewed Enrollment
    Then I should have no errors
    And The Enrollment should be in the 'Approved' state

  @ignore
  Scenario: Edit, save, and resubmit approved Speech Language Pathologist application
    Given I am logged in as a provider
    When I edit the Individual Enrollment '0000000006'
    And I save the application as a draft
    And I submit the enrollment
    Then I should have no errors
    And The Enrollment should be in the 'Pending' state

  @ignore
  Scenario: Edit, save, and resubmit approved Head Start application
    Given I am logged in as a provider
    When I edit the Organizational Enrollment '1234567893'
    And I save the application as a draft
    And I submit the enrollment
    Then I should have no errors
    And The Enrollment should be in the 'Pending' state
