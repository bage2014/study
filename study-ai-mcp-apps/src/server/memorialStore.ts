import { persistenceService } from './persistenceService';

export interface Memorial {
  id: string;
  familyId: string;
  memberId: string;
  memberName: string;
  birthDate: string;
  deathDate: string;
  epitaph: string | null;
  portraitUrl: string | null;
  obituary: string | null;
  memorialDate: string | null;
  createdAt: string;
  createdBy: string;
}

class MemorialStore {
  private memorials: Memorial[] = [];

  constructor() {
    const savedMemorials = persistenceService.getMemorials();
    
    if (savedMemorials.length > 0) {
      this.memorials = savedMemorials;
    } else {
      this.memorials.push({
      id: 'memorial-1',
      familyId: 'family-1',
      memberId: 'member-1',
      memberName: '张建国',
      birthDate: '1950-05-15',
      deathDate: '2020-12-20',
      epitaph: '一生勤劳，德高望重，福泽后人',
      portraitUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=elderly%20chinese%20man%20portrait%20vintage&image_size=portrait_4_3',
      obituary: '张建国同志于2020年12月20日安详离世，享年70岁。他一生勤劳朴实，为家族和村集体做出了巨大贡献。',
      memorialDate: '2024-04-05',
      createdAt: '2024-01-01',
      createdBy: 'user-1',
    });

    this.memorials.push({
      id: 'memorial-2',
      familyId: 'family-2',
      memberId: 'member-9',
      memberName: '王明德',
      birthDate: '1945-03-10',
      deathDate: '2018-09-15',
      epitaph: '忠厚传家远，诗书继世长',
      portraitUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=elderly%20chinese%20man%20formal%20portrait&image_size=portrait_4_3',
      obituary: '王明德同志于2018年9月15日逝世，享年73岁。他曾任工厂厂长，为家族事业奋斗终身。',
      memorialDate: '2024-04-05',
      createdAt: '2024-01-02',
      createdBy: 'user-2',
    });

      persistenceService.setMemorials(this.memorials);
    }
  }

  getAllMemorials(): Memorial[] {
    return this.memorials;
  }

  getMemorialsByFamily(familyId: string): Memorial[] {
    return this.memorials.filter(m => m.familyId === familyId);
  }

  getMemorialById(memorialId: string): Memorial | undefined {
    return this.memorials.find(m => m.id === memorialId);
  }

  getMemorialByMember(memberId: string): Memorial | undefined {
    return this.memorials.find(m => m.memberId === memberId);
  }

  createMemorial(data: {
    familyId: string;
    memberId: string;
    memberName: string;
    birthDate: string;
    deathDate: string;
    epitaph?: string | null;
    portraitUrl?: string | null;
    obituary?: string | null;
    memorialDate?: string | null;
    createdBy: string;
  }): Memorial {
    const memorial: Memorial = {
      id: 'memorial-' + Date.now(),
      ...data,
      epitaph: data.epitaph || null,
      portraitUrl: data.portraitUrl || null,
      obituary: data.obituary || null,
      memorialDate: data.memorialDate || null,
      createdAt: new Date().toISOString().split('T')[0],
    };
    this.memorials.push(memorial);
    persistenceService.setMemorials(this.memorials);
    return memorial;
  }

  updateMemorial(memorialId: string, data: Partial<Memorial>): Memorial | undefined {
    const index = this.memorials.findIndex(m => m.id === memorialId);
    if (index === -1) return undefined;
    this.memorials[index] = {
      ...this.memorials[index],
      ...data,
    };
    persistenceService.setMemorials(this.memorials);
    return this.memorials[index];
  }

  deleteMemorial(memorialId: string): boolean {
    const index = this.memorials.findIndex(m => m.id === memorialId);
    if (index === -1) return false;
    this.memorials.splice(index, 1);
    persistenceService.setMemorials(this.memorials);
    return true;
  }
}

export const memorialStore = new MemorialStore();
