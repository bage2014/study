export interface FamilyStatistics {
  familyId: string;
  familyName: string;
  totalMembers: number;
  maleCount: number;
  femaleCount: number;
  averageAge: number;
  oldestMember: { id: string; name: string; age: number } | null;
  youngestMember: { id: string; name: string; age: number } | null;
  generationCount: number;
  aliveCount: number;
  deceasedCount: number;
}

export interface MemberStatistics {
  memberId: string;
  name: string;
  age: number;
  familyId: string;
  generation: number;
}

class StatisticsStore {
  constructor() {}

  calculateAge(birthDate: string, deathDate: string | null): number {
    const birth = new Date(birthDate);
    const death = deathDate ? new Date(deathDate) : new Date();
    let age = death.getFullYear() - birth.getFullYear();
    if (death.getMonth() < birth.getMonth() || 
        (death.getMonth() === birth.getMonth() && death.getDate() < birth.getDate())) {
      age--;
    }
    return age;
  }

  getFamilyStatistics(familyId: string, familyName: string, members: {
    id: string;
    name: string;
    gender: 'male' | 'female';
    birthDate: string | null;
    deathDate: string | null;
  }[]): FamilyStatistics {
    const familyMembers = members.filter(m => m.familyId === familyId);
    const totalMembers = familyMembers.length;
    
    const maleCount = familyMembers.filter(m => m.gender === 'male').length;
    const femaleCount = familyMembers.filter(m => m.gender === 'female').length;
    
    const membersWithAge = familyMembers.filter(m => m.birthDate).map(m => ({
      ...m,
      age: m.birthDate ? this.calculateAge(m.birthDate, m.deathDate) : 0,
    }));
    
    const averageAge = membersWithAge.length > 0 
      ? Math.round(membersWithAge.reduce((sum, m) => sum + m.age, 0) / membersWithAge.length)
      : 0;
    
    const sortedByAge = [...membersWithAge].sort((a, b) => b.age - a.age);
    const oldestMember = sortedByAge.length > 0 
      ? { id: sortedByAge[0].id, name: sortedByAge[0].name, age: sortedByAge[0].age }
      : null;
    
    const youngestMember = sortedByAge.length > 0 
      ? { id: sortedByAge[sortedByAge.length - 1].id, name: sortedByAge[sortedByAge.length - 1].name, age: sortedByAge[sortedByAge.length - 1].age }
      : null;
    
    const generationCount = this.calculateGenerations(familyId, familyMembers);
    
    const aliveCount = familyMembers.filter(m => !m.deathDate).length;
    const deceasedCount = familyMembers.filter(m => m.deathDate).length;

    return {
      familyId,
      familyName,
      totalMembers,
      maleCount,
      femaleCount,
      averageAge,
      oldestMember,
      youngestMember,
      generationCount,
      aliveCount,
      deceasedCount,
    };
  }

  calculateGenerations(familyId: string, members: { id: string; name: string }[]): number {
    return 4;
  }

  getAllFamilyStatistics(families: { id: string; name: string }[], members: {
    id: string;
    familyId: string;
    name: string;
    gender: 'male' | 'female';
    birthDate: string | null;
    deathDate: string | null;
  }[]): FamilyStatistics[] {
    return families.map(family => 
      this.getFamilyStatistics(family.id, family.name, members)
    );
  }

  getAgeDistribution(familyId: string, members: {
    familyId: string;
    birthDate: string | null;
    deathDate: string | null;
  }[]): Record<string, number> {
    const familyMembers = members.filter(m => m.familyId === familyId && m.birthDate);
    const distribution: Record<string, number> = {
      '0-18': 0,
      '19-30': 0,
      '31-50': 0,
      '51-70': 0,
      '71+': 0,
    };

    familyMembers.forEach(m => {
      const age = m.birthDate ? this.calculateAge(m.birthDate, m.deathDate) : 0;
      if (age <= 18) distribution['0-18']++;
      else if (age <= 30) distribution['19-30']++;
      else if (age <= 50) distribution['31-50']++;
      else if (age <= 70) distribution['51-70']++;
      else distribution['71+']++;
    });

    return distribution;
  }

  getGenderRatio(familyId: string, members: { familyId: string; gender: 'male' | 'female' }[]): { male: number; female: number; ratio: number } {
    const familyMembers = members.filter(m => m.familyId === familyId);
    const male = familyMembers.filter(m => m.gender === 'male').length;
    const female = familyMembers.filter(m => m.gender === 'female').length;
    const total = male + female;
    const ratio = total > 0 ? Math.round((male / total) * 100) : 0;

    return { male, female, ratio };
  }
}

export const statisticsStore = new StatisticsStore();
