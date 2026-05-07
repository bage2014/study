import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from '../utils/axios';

export const useImageParseStore = defineStore('imageParse', () => {
  const parseResult = ref(null);
  const isLoading = ref(false);

  const uploadAndParse = async (file, familyId) => {
    isLoading.value = true;
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('familyId', familyId);

      const response = await axios.post('/api/image-parse/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      if (response.data.code === 200 && response.data.data) {
        parseResult.value = response.data.data;
        return response.data.data;
      } else {
        throw new Error(response.data.message || '解析失败');
      }
    } catch (error) {
      throw new Error(error.response?.data?.message || error.message || '解析失败');
    } finally {
      isLoading.value = false;
    }
  };

  const parseBase64Image = async (imageBase64, imageName, familyId) => {
    isLoading.value = true;
    try {
      const response = await axios.post('/api/image-parse/parse', {
        familyId,
        imageBase64,
        imageName
      });

      if (response.data.code === 200 && response.data.data) {
        parseResult.value = response.data.data;
        return response.data.data;
      } else {
        throw new Error(response.data.message || '解析失败');
      }
    } catch (error) {
      throw new Error(error.response?.data?.message || error.message || '解析失败');
    } finally {
      isLoading.value = false;
    }
  };

  const saveParseResult = async (familyId, members, relationships) => {
    isLoading.value = true;
    try {
      const response = await axios.post('/api/image-parse/save', {
        familyId,
        members,
        relationships
      });

      if (response.data.code === 200 && response.data.data) {
        return response.data.data;
      } else {
        throw new Error(response.data.message || '保存失败');
      }
    } catch (error) {
      throw new Error(error.response?.data?.message || error.message || '保存失败');
    } finally {
      isLoading.value = false;
    }
  };

  const reset = () => {
    parseResult.value = null;
  };

  return {
    parseResult,
    isLoading,
    uploadAndParse,
    parseBase64Image,
    saveParseResult,
    reset
  };
});