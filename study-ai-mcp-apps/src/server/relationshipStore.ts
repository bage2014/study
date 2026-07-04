export type RelationshipType = 'father' | 'mother' | 'husband' | 'wife' | 'son' | 'daughter' | 'brother' | 'sister' | 'grandfather' | 'grandmother' | 'grandson' | 'granddaughter' | 'uncle' | 'aunt' | 'nephew' | 'niece' | 'cousin';

export interface Relationship {
  id: string;
  memberId1: string;
  memberId2: string;
  relationshipType: RelationshipType;
  createdAt: string;
}

class RelationshipStore {
  private relationships: Relationship[] = [];

  constructor() {
    this.relationships.push({
      id: 'rel-1',
      memberId1: 'member-1',
      memberId2: 'member-3',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-2',
      memberId1: 'member-2',
      memberId2: 'member-3',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-3',
      memberId1: 'member-1',
      memberId2: 'member-6',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-4',
      memberId1: 'member-2',
      memberId2: 'member-6',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-5',
      memberId1: 'member-3',
      memberId2: 'member-4',
      relationshipType: 'wife',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-6',
      memberId1: 'member-3',
      memberId2: 'member-5',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-7',
      memberId1: 'member-4',
      memberId2: 'member-5',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-8',
      memberId1: 'member-6',
      memberId2: 'member-7',
      relationshipType: 'husband',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-9',
      memberId1: 'member-6',
      memberId2: 'member-8',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-10',
      memberId1: 'member-7',
      memberId2: 'member-8',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-11',
      memberId1: 'member-3',
      memberId2: 'member-6',
      relationshipType: 'brother',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-12',
      memberId1: 'member-9',
      memberId2: 'member-11',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-13',
      memberId1: 'member-10',
      memberId2: 'member-11',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-14',
      memberId1: 'member-9',
      memberId2: 'member-14',
      relationshipType: 'daughter',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-15',
      memberId1: 'member-10',
      memberId2: 'member-14',
      relationshipType: 'daughter',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-16',
      memberId1: 'member-11',
      memberId2: 'member-12',
      relationshipType: 'wife',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-17',
      memberId1: 'member-11',
      memberId2: 'member-13',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-18',
      memberId1: 'member-12',
      memberId2: 'member-13',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-19',
      memberId1: 'member-11',
      memberId2: 'member-14',
      relationshipType: 'brother',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-20',
      memberId1: 'member-15',
      memberId2: 'member-17',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-21',
      memberId1: 'member-16',
      memberId2: 'member-17',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-22',
      memberId1: 'member-15',
      memberId2: 'member-20',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-23',
      memberId1: 'member-16',
      memberId2: 'member-20',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-24',
      memberId1: 'member-17',
      memberId2: 'member-18',
      relationshipType: 'wife',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-25',
      memberId1: 'member-17',
      memberId2: 'member-19',
      relationshipType: 'daughter',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-26',
      memberId1: 'member-18',
      memberId2: 'member-19',
      relationshipType: 'daughter',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-27',
      memberId1: 'member-17',
      memberId2: 'member-20',
      relationshipType: 'brother',
      createdAt: '2024-01-03',
    });
  }

  getAllRelationships(): Relationship[] {
    return this.relationships;
  }

  getRelationshipsByFamily(familyId: string, members: { id: string; familyId: string }[]): Relationship[] {
    const familyMemberIds = members.filter(m => m.familyId === familyId).map(m => m.id);
    return this.relationships.filter(r =>
      familyMemberIds.includes(r.memberId1) && familyMemberIds.includes(r.memberId2)
    );
  }

  getRelationshipsByMember(memberId: string): Relationship[] {
    return this.relationships.filter(r => r.memberId1 === memberId || r.memberId2 === memberId);
  }

  getRelationship(id: string): Relationship | undefined {
    return this.relationships.find(r => r.id === id);
  }

  createRelationship(memberId1: string, memberId2: string, relationshipType: RelationshipType): Relationship {
    const relationship: Relationship = {
      id: 'rel-' + Date.now(),
      memberId1,
      memberId2,
      relationshipType,
      createdAt: new Date().toISOString().split('T')[0],
    };
    this.relationships.push(relationship);
    return relationship;
  }

  deleteRelationship(relationshipId: string): boolean {
    const index = this.relationships.findIndex(r => r.id === relationshipId);
    if (index === -1) return false;
    this.relationships.splice(index, 1);
    return true;
  }

  deleteRelationshipsByMember(memberId: string): void {
    this.relationships = this.relationships.filter(r => r.memberId1 !== memberId && r.memberId2 !== memberId);
  }

  getRelationshipLabel(type: RelationshipType): string {
    const labels: Record<RelationshipType, string> = {
      father: '父亲',
      mother: '母亲',
      husband: '丈夫',
      wife: '妻子',
      son: '儿子',
      daughter: '女儿',
      brother: '兄弟',
      sister: '姐妹',
      grandfather: '祖父',
      grandmother: '祖母',
      grandson: '孙子',
      granddaughter: '孙女',
      uncle: '叔叔',
      aunt: '姑姑',
      nephew: '侄子',
      niece: '侄女',
      cousin: '堂/表兄弟姐妹',
    };
    return labels[type];
  }
}

export const relationshipStore = new RelationshipStore();