const Map<String, dynamic> familyDataMap = {
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "avatar": "https://example.com/avatars/zhangsan.jpg",
    "generation": 0,
    "relationship": "self",
    "children": [
      {
        "id": 2,
        "name": "李四",
        "avatar": "https://example.com/avatars/lisi.jpg",
        "generation": 0,
        "relationship": "SPOUSE",
        "children": null,
      },
      {
        "id": 3,
        "name": "张父",
        "avatar": "https://example.com/avatars/father.jpg",
        "generation": -1,
        "relationship": "PARENT_CHILD",
        "children": [
          {
            "id": 4,
            "name": "张母",
            "avatar": "https://example.com/avatars/mother.jpg",
            "generation": -1,
            "relationship": "SPOUSE",
            "children": null,
          },
          {
            "id": 5,
            "name": "张祖父",
            "avatar": "https://example.com/avatars/grandfather.jpg",
            "generation": -2,
            "relationship": "PARENT_CHILD",
            "children": null,
          },
        ],
      },
      {
        "id": 6,
        "name": "张小三",
        "avatar": "https://example.com/avatars/xiaosan.jpg",
        "generation": 1,
        "relationship": "PARENT_CHILD",
        "children": null,
      },
    ],
  },
};

const Map<String, dynamic> familyData2 = {
  "id": "123",
  "name": "张三",
  "avatar":
      "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
  "generation": 0,
  "relationship": "self",
  "spouses": [
    {
      "id": "124",
      "name": "李四",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "spouse",
      "generation": 0,
      "spouses": [],
      "parents": [],
    },
  ],
  "parents": [
    {
      "id": "111",
      "name": "张父",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "parent",
      "generation": -1,
      "spouses": [
        {
          "id": "112",
          "name": "张母",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "spouse",
          "generation": -1,
        },
      ],
      "parents": [
        {
          "id": "101",
          "name": "张祖父",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "parent",
          "generation": -2,
        },
      ],
    },
  ],
  "children": [
    {
      "id": "125",
      "name": "张小三",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "relationship": "child",
      "generation": 1,
      "spouses": [],
      "children": [],
    },
  ],
};
