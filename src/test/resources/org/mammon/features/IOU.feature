Feature: IOU
  In order to keep track of outstanding debts
  As a member of a social group
  I want my IOU's to be correctly administrated

  Scenario: Create an IOU
    Given Bob owes Alice 12.00 euros
    When Bob creates an IOU of 12.00 euros
    And sends the IOU to Alice
    Then Alice should have the IOU [Bob, 12.00]
    
  Scenario: List IOUs
    Given Alice holds the IOU [Bob, 1.00]
    And Alice holds the IOU [Carol, 2.00]
    And Alice holds the IOU [Dave, 3.00]
    When Alice lists the IOU's
    Then Alice should have the IOU [Bob, 1.00]
    And Alice should have the IOU [Coral, 2.00]
    And Alice should have the IOU [Dave, 3.00]
  
  Scenario: Redeem an IOU
    Given Alice holds the IOU [Bob, 1.00]
    And Alice holds the IOU [Caral, 2.00]
    When Alice redeems the IOU [Bob, 1.00]
    Then Alice should have the IOU [Coral, 2.00]
    
  Scenario: Forward an IOU
    Given Alice owes Carol 3.00 euros
    And Alice holds the IOU [Bob, 3.00]
    When Alice forwards the IOU [Bob, 3.00] to Carol
    Then Alice should have no IOU's
    And Coral should have the IOU [Bob, 3.00]
  