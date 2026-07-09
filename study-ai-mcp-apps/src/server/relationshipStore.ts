import { persistenceService } from './persistenceService';

export type RelationshipType = 'father' | 'mother' | 'husband' | 'wife' | 'son' | 'daughter' | 'brother' | 'sister' | 'grandfather' | 'grandmother' | 'grandson' | 'granddaughter' | 'uncle' | 'aunt' | 'nephew' | 'niece' | 'cousin';

export interface Relationship {
  id: string;
  memberId1: string;
  memberId2: string;
  relationshipType: RelationshipType;
  createdAt: string;
}

interface ValidationResult {
  valid: boolean;
  message: string;
}

const spouseTypes: RelationshipType[] = ['husband', 'wife'];
const parentTypes: RelationshipType[] = ['father', 'mother'];
const childTypes: RelationshipType[] = ['son', 'daughter'];
const maleSpouseTypes: RelationshipType[] = ['husband'];
const femaleSpouseTypes: RelationshipType[] = ['wife'];
const maleParentTypes: RelationshipType[] = ['father'];
const femaleParentTypes: RelationshipType[] = ['mother'];

class RelationshipStore {
  private relationships: Relationship[] = [];

  constructor() {
    const savedRelationships = persistenceService.getRelationships();
    
    if (savedRelationships.length > 0) {
      this.relationships = savedRelationships;
    } else {
      this.relationships.push({
      id: 'rel-1',
      memberId1: 'member-1',
      memberId2: 'member-2',
      relationshipType: 'husband',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-2',
      memberId1: 'member-1',
      memberId2: 'member-3',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-3',
      memberId1: 'member-2',
      memberId2: 'member-3',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-4',
      memberId1: 'member-1',
      memberId2: 'member-6',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-5',
      memberId1: 'member-2',
      memberId2: 'member-6',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-6',
      memberId1: 'member-3',
      memberId2: 'member-4',
      relationshipType: 'husband',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-7',
      memberId1: 'member-3',
      memberId2: 'member-5',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-8',
      memberId1: 'member-4',
      memberId2: 'member-5',
      relationshipType: 'son',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-9',
      memberId1: 'member-6',
      memberId2: 'member-7',
      relationshipType: 'husband',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-10',
      memberId1: 'member-6',
      memberId2: 'member-8',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-11',
      memberId1: 'member-7',
      memberId2: 'member-8',
      relationshipType: 'daughter',
      createdAt: '2024-01-01',
    });

    this.relationships.push({
      id: 'rel-12',
      memberId1: 'member-9',
      memberId2: 'member-10',
      relationshipType: 'husband',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-13',
      memberId1: 'member-9',
      memberId2: 'member-11',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-14',
      memberId1: 'member-10',
      memberId2: 'member-11',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-15',
      memberId1: 'member-9',
      memberId2: 'member-14',
      relationshipType: 'daughter',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-16',
      memberId1: 'member-10',
      memberId2: 'member-14',
      relationshipType: 'daughter',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-17',
      memberId1: 'member-11',
      memberId2: 'member-12',
      relationshipType: 'husband',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-18',
      memberId1: 'member-11',
      memberId2: 'member-13',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-19',
      memberId1: 'member-12',
      memberId2: 'member-13',
      relationshipType: 'son',
      createdAt: '2024-01-02',
    });

    this.relationships.push({
      id: 'rel-20',
      memberId1: 'member-15',
      memberId2: 'member-16',
      relationshipType: 'husband',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-21',
      memberId1: 'member-15',
      memberId2: 'member-17',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-22',
      memberId1: 'member-16',
      memberId2: 'member-17',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-23',
      memberId1: 'member-15',
      memberId2: 'member-20',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-24',
      memberId1: 'member-16',
      memberId2: 'member-20',
      relationshipType: 'son',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-25',
      memberId1: 'member-17',
      memberId2: 'member-18',
      relationshipType: 'husband',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-26',
      memberId1: 'member-17',
      memberId2: 'member-19',
      relationshipType: 'daughter',
      createdAt: '2024-01-03',
    });

    this.relationships.push({
      id: 'rel-27',
      memberId1: 'member-18',
      memberId2: 'member-19',
      relationshipType: 'daughter',
      createdAt: '2024-01-03',
    });

      persistenceService.setRelationships(this.relationships);
    }
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

  validateRelationship(
    memberId1: string,
    memberId2: string,
    relationshipType: RelationshipType,
    gender1: 'male' | 'female' | undefined,
    gender2: 'male' | 'female' | undefined
  ): ValidationResult {
    if (memberId1 === memberId2) {
      return { valid: false, message: '不能创建自己与自己的关系' };
    }

    const existingRel = this.relationships.find(
      r => (r.memberId1 === memberId1 && r.memberId2 === memberId2) ||
           (r.memberId1 === memberId2 && r.memberId2 === memberId1)
    );
    if (existingRel) {
      return { valid: false, message: '这两个成员之间已存在关系' };
    }

    if (spouseTypes.includes(relationshipType)) {
      if (!gender1 || !gender2) {
        return { valid: false, message: '创建配偶关系需要双方性别信息' };
      }
      if (gender1 === gender2) {
        return { valid: false, message: '配偶关系必须是不同性别' };
      }
      if (gender1 === 'male' && !maleSpouseTypes.includes(relationshipType)) {
        return { valid: false, message: '男性不能创建妻子关系' };
      }
      if (gender1 === 'female' && !femaleSpouseTypes.includes(relationshipType)) {
        return { valid: false, message: '女性不能创建丈夫关系' };
      }
    }

    if (parentTypes.includes(relationshipType)) {
      if (!gender1) {
        return { valid: false, message: '创建父母关系需要性别信息' };
      }
      if (gender1 === 'male' && !maleParentTypes.includes(relationshipType)) {
        return { valid: false, message: '男性不能创建母亲关系' };
      }
      if (gender1 === 'female' && !femaleParentTypes.includes(relationshipType)) {
        return { valid: false, message: '女性不能创建父亲关系' };
      }

      const existingChildRel = this.relationships.find(
        r => r.memberId1 === memberId2 && r.memberId2 === memberId1 &&
             childTypes.includes(r.relationshipType)
      );
      if (existingChildRel) {
        return { valid: false, message: '不能同时创建父子/母子和子父/子母关系' };
      }
    }

    if (childTypes.includes(relationshipType)) {
      if (!gender2) {
        return { valid: false, message: '创建子女关系需要对方性别信息' };
      }
      if (gender2 === 'male' && relationshipType === 'daughter') {
        return { valid: false, message: '男性不能是女儿' };
      }
      if (gender2 === 'female' && relationshipType === 'son') {
        return { valid: false, message: '女性不能是儿子' };
      }

      const existingParentRel = this.relationships.find(
        r => r.memberId1 === memberId2 && r.memberId2 === memberId1 &&
             parentTypes.includes(r.relationshipType)
      );
      if (existingParentRel) {
        return { valid: false, message: '不能同时创建子父/子母和父子/母子关系' };
      }
    }

    return { valid: true, message: '' };
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
    persistenceService.setRelationships(this.relationships);
    return relationship;
  }

  deleteRelationship(relationshipId: string): boolean {
    const index = this.relationships.findIndex(r => r.id === relationshipId);
    if (index === -1) return false;
    this.relationships.splice(index, 1);
    persistenceService.setRelationships(this.relationships);
    return true;
  }

  deleteRelationshipsByMember(memberId: string): void {
    this.relationships = this.relationships.filter(r => r.memberId1 !== memberId && r.memberId2 !== memberId);
    persistenceService.setRelationships(this.relationships);
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

  getReverseRelationship(type: RelationshipType): RelationshipType {
    const reverses: Record<RelationshipType, RelationshipType> = {
      father: 'son',
      mother: 'son',
      husband: 'wife',
      wife: 'husband',
      son: 'father',
      daughter: 'father',
      brother: 'brother',
      sister: 'sister',
      grandfather: 'grandson',
      grandmother: 'grandson',
      grandson: 'grandfather',
      granddaughter: 'grandfather',
      uncle: 'nephew',
      aunt: 'nephew',
      nephew: 'uncle',
      niece: 'uncle',
      cousin: 'cousin',
    };
    return reverses[type];
  }

  calculateRelation(memberId1: string, memberId2: string, gender1: 'male' | 'female', gender2: 'male' | 'female'): { label: string; type: RelationshipType } | null {
    const rel = this.relationships.find(
      r => (r.memberId1 === memberId1 && r.memberId2 === memberId2) ||
           (r.memberId1 === memberId2 && r.memberId2 === memberId1)
    );

    if (!rel) {
      return { label: '未知关系', type: 'cousin' };
    }

    const isDirect = rel.memberId1 === memberId1;
    let type = isDirect ? rel.relationshipType : this.getReverseRelationship(rel.relationshipType);

    const adjustLabel = (baseType: RelationshipType, gender: 'male' | 'female'): { label: string; type: RelationshipType } => {
      const adjustments: Record<string, { male: string; female: string; maleType: RelationshipType; femaleType: RelationshipType }> = {
        son: { male: '儿子', female: '女儿', maleType: 'son', femaleType: 'daughter' },
        daughter: { male: '儿子', female: '女儿', maleType: 'son', femaleType: 'daughter' },
        brother: { male: '兄弟', female: '姐妹', maleType: 'brother', femaleType: 'sister' },
        sister: { male: '兄弟', female: '姐妹', maleType: 'brother', femaleType: 'sister' },
        grandson: { male: '孙子', female: '孙女', maleType: 'grandson', femaleType: 'granddaughter' },
        granddaughter: { male: '孙子', female: '孙女', maleType: 'grandson', femaleType: 'granddaughter' },
        nephew: { male: '侄子', female: '侄女', maleType: 'nephew', femaleType: 'niece' },
        niece: { male: '侄子', female: '侄女', maleType: 'nephew', femaleType: 'niece' },
        uncle: { male: '叔叔', female: '姑姑', maleType: 'uncle', femaleType: 'aunt' },
        aunt: { male: '叔叔', female: '姑姑', maleType: 'uncle', femaleType: 'aunt' },
        father: { male: '父亲', female: '母亲', maleType: 'father', femaleType: 'mother' },
        mother: { male: '父亲', female: '母亲', maleType: 'father', femaleType: 'mother' },
      };

      if (adjustments[baseType]) {
        return {
          label: adjustments[baseType][gender],
          type: adjustments[baseType][gender === 'male' ? 'maleType' : 'femaleType'],
        };
      }

      return { label: this.getRelationshipLabel(baseType), type: baseType };
    };

    if (type === 'son' || type === 'daughter' || type === 'brother' || type === 'sister') {
      return adjustLabel(type, gender2);
    }

    if (type === 'grandson' || type === 'granddaughter' || type === 'nephew' || type === 'niece') {
      return adjustLabel(type, gender2);
    }

    if (type === 'uncle' || type === 'aunt' || type === 'father' || type === 'mother') {
      return adjustLabel(type, gender2);
    }

    return { label: this.getRelationshipLabel(type), type };
  }
}

export const relationshipStore = new RelationshipStore();