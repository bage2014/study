import { persistenceService } from './persistenceService';

export interface Photo {
  id: string;
  familyId: string;
  albumId: string;
  title: string;
  description: string;
  url: string;
  thumbnailUrl: string;
  uploadedBy: string;
  uploadedAt: string;
  relatedMemberId: string | null;
}

export interface Album {
  id: string;
  familyId: string;
  name: string;
  description: string;
  coverPhotoId: string | null;
  createdAt: string;
  createdBy: string;
}

class AlbumStore {
  private albums: Album[] = [];
  private photos: Photo[] = [];

  constructor() {
    const savedAlbums = persistenceService.getAlbums();
    const savedPhotos = persistenceService.getPhotos();
    
    if (savedAlbums.length > 0) {
      this.albums = savedAlbums;
      this.photos = savedPhotos;
    } else {
      this.albums.push({
      id: 'album-1',
      familyId: 'family-1',
      name: '家族聚会',
      description: '2024年春节家族聚会照片',
      coverPhotoId: 'photo-1',
      createdAt: '2024-01-15',
      createdBy: 'user-1',
    });

    this.albums.push({
      id: 'album-2',
      familyId: 'family-1',
      name: '祖先纪念',
      description: '家族祖先照片和遗物',
      coverPhotoId: 'photo-4',
      createdAt: '2024-01-10',
      createdBy: 'user-1',
    });

    this.albums.push({
      id: 'album-3',
      familyId: 'family-2',
      name: '王氏家族庆典',
      description: '家族庆典活动照片',
      coverPhotoId: 'photo-7',
      createdAt: '2024-02-01',
      createdBy: 'user-2',
    });

    this.photos.push({
      id: 'photo-1',
      familyId: 'family-1',
      albumId: 'album-1',
      title: '全家福',
      description: '2024年春节全家福',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20family%20reunion%20photo%20spring%20festival&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20family%20reunion%20photo%20spring%20festival&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-15',
      relatedMemberId: 'member-1',
    });

    this.photos.push({
      id: 'photo-2',
      familyId: 'family-1',
      albumId: 'album-1',
      title: '年夜饭',
      description: '丰盛的年夜饭',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20new%20year%20dinner%20family%20gathering&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20new%20year%20dinner%20family%20gathering&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-15',
      relatedMemberId: null,
    });

    this.photos.push({
      id: 'photo-3',
      familyId: 'family-1',
      albumId: 'album-1',
      title: '孩子们',
      description: '家族下一代合影',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20children%20group%20photo%20happy&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20children%20group%20photo%20happy&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-15',
      relatedMemberId: 'member-5',
    });

    this.photos.push({
      id: 'photo-4',
      familyId: 'family-1',
      albumId: 'album-2',
      title: '祖父照片',
      description: '张建国年轻时的照片',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=elderly%20chinese%20man%20portrait%20vintage&image_size=portrait_4_3',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=elderly%20chinese%20man%20portrait%20vintage&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-10',
      relatedMemberId: 'member-1',
    });

    this.photos.push({
      id: 'photo-5',
      familyId: 'family-1',
      albumId: 'album-2',
      title: '族谱原件',
      description: '祖传族谱原件照片',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=ancient%20chinese%20family%20tree%20document&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=ancient%20chinese%20family%20tree%20document&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-10',
      relatedMemberId: null,
    });

    this.photos.push({
      id: 'photo-6',
      familyId: 'family-1',
      albumId: 'album-2',
      title: '祖屋',
      description: '家族祖屋照片',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=traditional%20chinese%20old%20house%20village&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=traditional%20chinese%20old%20house%20village&image_size=square',
      uploadedBy: 'user-1',
      uploadedAt: '2024-01-10',
      relatedMemberId: null,
    });

    this.photos.push({
      id: 'photo-7',
      familyId: 'family-2',
      albumId: 'album-3',
      title: '庆典合影',
      description: '王氏家族庆典全体合影',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20family%20celebration%20group%20photo&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20family%20celebration%20group%20photo&image_size=square',
      uploadedBy: 'user-2',
      uploadedAt: '2024-02-01',
      relatedMemberId: 'member-9',
    });

    this.photos.push({
      id: 'photo-8',
      familyId: 'family-2',
      albumId: 'album-3',
      title: '舞龙表演',
      description: '庆典舞龙表演',
      url: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20dragon%20dance%20celebration&image_size=landscape_16_9',
      thumbnailUrl: 'https://neeko-copilot.bytedance.net/api/text2image?prompt=chinese%20dragon%20dance%20celebration&image_size=square',
      uploadedBy: 'user-2',
      uploadedAt: '2024-02-01',
      relatedMemberId: null,
    });

      persistenceService.setAlbums(this.albums);
      persistenceService.setPhotos(this.photos);
    }
  }

  getAllAlbums(): Album[] {
    return this.albums;
  }

  getAlbumsByFamily(familyId: string): Album[] {
    return this.albums.filter(a => a.familyId === familyId);
  }

  getAlbumById(albumId: string): Album | undefined {
    return this.albums.find(a => a.id === albumId);
  }

  createAlbum(familyId: string, name: string, description: string, createdBy: string): Album {
    const album: Album = {
      id: 'album-' + Date.now(),
      familyId,
      name,
      description,
      coverPhotoId: null,
      createdAt: new Date().toISOString().split('T')[0],
      createdBy,
    };
    this.albums.push(album);
    persistenceService.setAlbums(this.albums);
    return album;
  }

  updateAlbum(albumId: string, name: string, description: string): Album | undefined {
    const index = this.albums.findIndex(a => a.id === albumId);
    if (index === -1) return undefined;
    this.albums[index] = {
      ...this.albums[index],
      name,
      description,
    };
    persistenceService.setAlbums(this.albums);
    return this.albums[index];
  }

  deleteAlbum(albumId: string): boolean {
    const index = this.albums.findIndex(a => a.id === albumId);
    if (index === -1) return false;
    this.photos = this.photos.filter(p => p.albumId !== albumId);
    this.albums.splice(index, 1);
    persistenceService.setAlbums(this.albums);
    persistenceService.setPhotos(this.photos);
    return true;
  }

  getAllPhotos(): Photo[] {
    return this.photos;
  }

  getPhotosByAlbum(albumId: string): Photo[] {
    return this.photos.filter(p => p.albumId === albumId);
  }

  getPhotosByFamily(familyId: string): Photo[] {
    return this.photos.filter(p => p.familyId === familyId);
  }

  getPhotoById(photoId: string): Photo | undefined {
    return this.photos.find(p => p.id === photoId);
  }

  createPhoto(data: {
    familyId: string;
    albumId: string;
    title: string;
    description: string;
    url: string;
    thumbnailUrl: string;
    uploadedBy: string;
    relatedMemberId: string | null;
  }): Photo {
    const photo: Photo = {
      id: 'photo-' + Date.now(),
      ...data,
      uploadedAt: new Date().toISOString().split('T')[0],
    };
    this.photos.push(photo);

    const album = this.getAlbumById(data.albumId);
    if (album && !album.coverPhotoId) {
      album.coverPhotoId = photo.id;
    }

    persistenceService.setAlbums(this.albums);
    persistenceService.setPhotos(this.photos);
    return photo;
  }

  updatePhoto(photoId: string, data: Partial<Photo>): Photo | undefined {
    const index = this.photos.findIndex(p => p.id === photoId);
    if (index === -1) return undefined;
    this.photos[index] = {
      ...this.photos[index],
      ...data,
    };
    persistenceService.setPhotos(this.photos);
    return this.photos[index];
  }

  deletePhoto(photoId: string): boolean {
    const index = this.photos.findIndex(p => p.id === photoId);
    if (index === -1) return false;

    const photo = this.photos[index];
    const album = this.getAlbumById(photo.albumId);
    if (album && album.coverPhotoId === photoId) {
      const otherPhotos = this.getPhotosByAlbum(photo.albumId).filter(p => p.id !== photoId);
      album.coverPhotoId = otherPhotos.length > 0 ? otherPhotos[0].id : null;
    }

    this.photos.splice(index, 1);
    persistenceService.setAlbums(this.albums);
    persistenceService.setPhotos(this.photos);
    return true;
  }
}

export const albumStore = new AlbumStore();
