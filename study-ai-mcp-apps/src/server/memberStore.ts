export interface Member {
  id: string;
  familyId: string;
  name: string;
  gender: 'male' | 'female';
  birthDate: string | null;
  deathDate: string | null;
  photo: string | null;
  details: string | null;
  phone: string | null;
  email: string | null;
  createdAt: string;
}

class MemberStore {
  private members: Member[] = [];

  constructor() {
    this.members.push({
      id: 'member-1',
      familyId: 'family-1',
      name: '张建国',
      gender: 'male',
      birthDate: '1950-05-15',
      deathDate: '2020-12-20',
      photo: null,
      details: '张氏家族第三代传人，曾任村支书',
      phone: '13900139001',
      email: 'zhangjianguo@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-2',
      familyId: 'family-1',
      name: '李秀英',
      gender: 'female',
      birthDate: '1952-08-20',
      deathDate: null,
      photo: null,
      details: '张建国之妻，勤劳善良',
      phone: '13900139002',
      email: 'lixiuying@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-3',
      familyId: 'family-1',
      name: '张伟',
      gender: 'male',
      birthDate: '1975-03-10',
      deathDate: null,
      photo: null,
      details: '张建国长子，现居北京',
      phone: '13800138001',
      email: 'zhangwei@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-4',
      familyId: 'family-1',
      name: '王芳',
      gender: 'female',
      birthDate: '1978-06-15',
      deathDate: null,
      photo: null,
      details: '张伟之妻，教师',
      phone: '13800138002',
      email: 'wangfang@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-5',
      familyId: 'family-1',
      name: '张小明',
      gender: 'male',
      birthDate: '2005-11-20',
      deathDate: null,
      photo: null,
      details: '张伟之子，中学生',
      phone: null,
      email: null,
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-6',
      familyId: 'family-1',
      name: '张丽',
      gender: 'female',
      birthDate: '1980-09-05',
      deathDate: null,
      photo: null,
      details: '张建国之女，医生',
      phone: '13800138003',
      email: 'zhangli@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-7',
      familyId: 'family-1',
      name: '李明',
      gender: 'male',
      birthDate: '1979-04-12',
      deathDate: null,
      photo: null,
      details: '张丽之夫，工程师',
      phone: '13800138004',
      email: 'liming@family.com',
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-8',
      familyId: 'family-1',
      name: '李婷婷',
      gender: 'female',
      birthDate: '2010-07-08',
      deathDate: null,
      photo: null,
      details: '张丽之女，小学生',
      phone: null,
      email: null,
      createdAt: '2024-01-01',
    });

    this.members.push({
      id: 'member-9',
      familyId: 'family-2',
      name: '王明德',
      gender: 'male',
      birthDate: '1945-03-10',
      deathDate: '2018-09-15',
      photo: null,
      details: '王氏家族族长，曾任工厂厂长',
      phone: '13900139001',
      email: 'wangmingde@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-10',
      familyId: 'family-2',
      name: '陈桂兰',
      gender: 'female',
      birthDate: '1947-07-22',
      deathDate: null,
      photo: null,
      details: '王明德之妻，家庭主妇',
      phone: '13900139002',
      email: 'chenguilan@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-11',
      familyId: 'family-2',
      name: '王伟',
      gender: 'male',
      birthDate: '1970-05-18',
      deathDate: null,
      photo: null,
      details: '王明德长子，企业家',
      phone: '13800138005',
      email: 'wangwei@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-12',
      familyId: 'family-2',
      name: '刘燕',
      gender: 'female',
      birthDate: '1972-11-08',
      deathDate: null,
      photo: null,
      details: '王伟之妻，会计师',
      phone: '13800138006',
      email: 'liuyan@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-13',
      familyId: 'family-2',
      name: '王小强',
      gender: 'male',
      birthDate: '1995-08-25',
      deathDate: null,
      photo: null,
      details: '王伟之子，程序员',
      phone: '13800138007',
      email: 'wangxiaoqiang@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-14',
      familyId: 'family-2',
      name: '王丽',
      gender: 'female',
      birthDate: '1975-02-14',
      deathDate: null,
      photo: null,
      details: '王明德之女，医生',
      phone: '13800138008',
      email: 'wangli@family.com',
      createdAt: '2024-01-02',
    });

    this.members.push({
      id: 'member-15',
      familyId: 'family-3',
      name: '李祥',
      gender: 'male',
      birthDate: '1952-06-15',
      deathDate: null,
      photo: null,
      details: '李氏家族族长，教师退休',
      phone: '13700137001',
      email: 'lixian@family.com',
      createdAt: '2024-01-03',
    });

    this.members.push({
      id: 'member-16',
      familyId: 'family-3',
      name: '赵秀芬',
      gender: 'female',
      birthDate: '1954-09-30',
      deathDate: null,
      photo: null,
      details: '李祥之妻，护士',
      phone: '13700137002',
      email: 'zhaoxiufen@family.com',
      createdAt: '2024-01-03',
    });

    this.members.push({
      id: 'member-17',
      familyId: 'family-3',
      name: '李刚',
      gender: 'male',
      birthDate: '1980-04-10',
      deathDate: null,
      photo: null,
      details: '李祥长子，工程师',
      phone: '13600136001',
      email: 'ligang@family.com',
      createdAt: '2024-01-03',
    });

    this.members.push({
      id: 'member-18',
      familyId: 'family-3',
      name: '孙静',
      gender: 'female',
      birthDate: '1982-07-18',
      deathDate: null,
      photo: null,
      details: '李刚之妻，设计师',
      phone: '13600136002',
      email: 'sunjing@family.com',
      createdAt: '2024-01-03',
    });

    this.members.push({
      id: 'member-19',
      familyId: 'family-3',
      name: '李悦',
      gender: 'female',
      birthDate: '2008-12-05',
      deathDate: null,
      photo: null,
      details: '李刚之女，中学生',
      phone: null,
      email: null,
      createdAt: '2024-01-03',
    });

    this.members.push({
      id: 'member-20',
      familyId: 'family-3',
      name: '李勇',
      gender: 'male',
      birthDate: '1985-03-20',
      deathDate: null,
      photo: null,
      details: '李祥次子，律师',
      phone: '13600136003',
      email: 'liyong@family.com',
      createdAt: '2024-01-03',
    });
  }

  getAllMembers(): Member[] {
    return this.members;
  }

  getMembersByFamily(familyId: string): Member[] {
    return this.members.filter(m => m.familyId === familyId);
  }

  getMemberById(memberId: string): Member | undefined {
    return this.members.find(m => m.id === memberId);
  }

  createMember(data: {
    familyId: string;
    name: string;
    gender: 'male' | 'female';
    birthDate: string | null;
    deathDate: string | null;
    photo: string | null;
    details: string | null;
    phone: string | null;
    email: string | null;
  }): Member {
    const member: Member = {
      id: 'member-' + Date.now(),
      ...data,
      createdAt: new Date().toISOString().split('T')[0],
    };
    this.members.push(member);
    return member;
  }

  updateMember(memberId: string, data: Partial<Member>): Member | undefined {
    const index = this.members.findIndex(m => m.id === memberId);
    if (index === -1) return undefined;
    this.members[index] = {
      ...this.members[index],
      ...data,
    };
    return this.members[index];
  }

  deleteMember(memberId: string): boolean {
    const index = this.members.findIndex(m => m.id === memberId);
    if (index === -1) return false;
    this.members.splice(index, 1);
    return true;
  }

  searchMembers(keyword: string, familyId?: string): Member[] {
    let result = this.members;
    if (familyId) {
      result = result.filter(m => m.familyId === familyId);
    }
    const lowerKeyword = keyword.toLowerCase();
    return result.filter(m =>
      m.name.toLowerCase().includes(lowerKeyword) ||
      m.phone?.includes(keyword) ||
      m.email?.toLowerCase().includes(lowerKeyword)
    );
  }
}

export const memberStore = new MemberStore();