public class UnionFind {
    int[] tr;

    public UnionFind(int n) {
        tr = new int[n];
        for (int i = 0; i < n; i++) {
            tr[i] = -1;
        }
    }

    public void validate(int v1) {
        if (v1 >= tr.length || v1 < 0) {
            throw new IllegalArgumentException("Index is invalid");
        }
    }

    public int sizeOf(int v1) {
        validate(v1);
        return -tr[find(v1)];
    }

    // 返回直接父节点（非根节点）
    public int parent(int v1) {
        validate(v1);
        return tr[v1];
    }

    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);

        if (find(v1) == find(v2)) {
            return true;
        } else {
            return false;
        }
    }

    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);

        int r1 = find(v1);
        int r2 = find(v2);

        if (r1 == r2) {
            return;
        }

        if (tr[r1] < tr[r2]) {
            tr[r1] += tr[r2];
            tr[r2] = r1;
        } else {
            tr[r2] += tr[r1];
            tr[r1] = r2;
        }
    }

    public int find(int v1) {
        validate(v1);

        if (tr[v1] < 0) {
            return v1;
        }

        tr[v1] = find(tr[v1]);
        return tr[v1];
    }
}