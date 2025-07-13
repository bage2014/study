// @ExtendWith(MockitoExtension.class)
// class FamilyControllerTest {
//     @Mock
//     private FamilyService familyService;
    
//     @InjectMocks
//     private FamilyController familyController;

//     private FamilyMember createRandomMember() {
//         FamilyMember member = new FamilyMember();
//         member.setId((long)(Math.random() * 1000));
//         member.setName("Member" + (int)(Math.random() * 100));
//         member.setGender(Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE);
//         return member;
//     }

//     @Test
//     void addMember_shouldHandleRandomData() {
//         FamilyMember randomMember = createRandomMember();
//         when(familyService.saveMember(any())).thenReturn(randomMember);
        
//         ApiResponse<?> response = familyController.addMember(randomMember);
        
//         assertEquals(200, response.getCode());
//         assertEquals(randomMember, response.getData());
//     }

//     @Test
//     void getMember_shouldReturnRandomMember() {
//         FamilyMember randomMember = createRandomMember();
//         when(familyService.getMemberById(anyLong())).thenReturn(randomMember);
        
//         ApiResponse<?> response = familyController.getMember(randomMember.getId());
        
//         assertEquals(200, response.getCode());
//         assertEquals(randomMember, response.getData());
//     }

//     @Test
//     void addRelationship_shouldValidateRandomRelationship() {
//         FamilyRelationship relationship = new FamilyRelationship();
//         relationship.setFromMemberId((long)(Math.random() * 1000));
//         relationship.setToMemberId((long)(Math.random() * 1000));
//         relationship.setType(Math.random() > 0.5 ? RelationshipType.PARENT : RelationshipType.SPOUSE);
        
//         when(familyService.saveRelationship(any())).thenReturn(relationship);
        
//         ApiResponse<?> response = familyController.addRelationship(relationship);
        
//         assertEquals(200, response.getCode());
//         verify(familyService).saveRelationship(relationship);
//     }

//     @Test
//     void getFamilyTree_shouldReturnRandomTree() {
//         FamilyMember root = createRandomMember();
//         when(familyService.getFamilyTree(anyLong(), anyInt())).thenReturn(root);
        
//         ApiResponse<?> response = familyController.getFamilyTree(root.getId(), 3);
        
//         assertEquals(200, response.getCode());
//         assertEquals(root, response.getData());
//     }

//     @Test
//     void initFamilyTree_shouldNotThrowWithRandomData() {
//         when(familyService.getMemberById(anyLong())).thenReturn(null);
//         assertDoesNotThrow(() -> familyController.initFamilyTree());
//     }
// }