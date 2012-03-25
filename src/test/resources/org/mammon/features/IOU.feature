Feature: IOU
  In order to keep track of outstanding debts
  As a member of a social group
  I want my IOU's to be correctly administrated

  Scenario: Create an IOU
    Given Bob owes Alice 12.00 euros
    When Bob creates an IOU of 12.00 euros
    And sends the IOU to Alice
    Then Alice should hold an IOU of 12.00 euros redeemable by Bob