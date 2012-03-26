Feature: IOU
  In order to keep track of outstanding debts
  As a member of a social group
  I want my IOU's to be correctly administrated

  Scenario: Create an IOU
    Given Bob owes Alice 12.00 euros
    When Bob creates an IOU of 12.00 euros
    And sends the IOU to Alice
    Then Alice should hold an IOU of 12.00 euros redeemable by Bob
    
  Scenario: List IOUs
    Given Alice holds an IOU of 1.00 euros redeemable by Bob
    And Alice holds an IOU of 2.00 euros redeemable by Carol
    And Alice holds an IOU of 3.00 euros redeemable by Dave
    When Alice lists the IOU's
    Then Alice should have the IOU [Bob, 1.00]
    And Alice should have the IOU [Coral, 2.00]
    And Alice should have the IOU [Dave, 3.00]